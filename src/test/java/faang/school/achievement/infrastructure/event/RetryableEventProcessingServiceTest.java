package faang.school.achievement.infrastructure.event;

import faang.school.achievement.exception.HandlerException;
import faang.school.achievement.exception.HandlersException;
import faang.school.achievement.handlers.EventHandler;
import faang.school.achievement.infrastructure.executor.HandlersExecutionStrategy;
import faang.school.achievement.infrastructure.kafka.HandlerRetry;
import faang.school.achievement.infrastructure.kafka.dlq.DlqMessageSender;
import faang.school.achievement.infrastructure.store.RetryCountStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetryableEventProcessingServiceTest {
    private static final int INT_VALUE_0 = 0;
    private static final int INT_VALUE_1 = 1;
    private static final long LONG_VALUE_3 = 3L;
    private final String event = "test-event";
    private final String eventKey = "event-key";
    private final String dlqTopicName = "dlq-topic";
    private final int maxRetryCount = 3;
    @Mock
    private EventHandler<String> handler1;
    @Mock
    private EventHandler<String> handler2;
    @Mock
    private HandlerRetry handlerRetry;
    @Mock
    private RetryCountStore<String, EventHandler<String>> retryCountStore;
    @Mock
    private HandlersExecutionStrategy<String, EventHandler<String>> handlersExecutionStrategy;
    @Mock
    private DlqMessageSender dlqMessageSender;
    @Mock
    private Acknowledgment acknowledgment;
    private RetryableEventProcessingService<String, EventHandler<String>> service;
    @Captor
    private ArgumentCaptor<Map<String, String>> errorMessagesCaptor;

    @BeforeEach
    void setUp() {
        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(INT_VALUE_0);
            runnable.run();
            return null;
        }).when(handlerRetry).execute(any());
    }


    @Test
    void process_SuccessfulFirstAttempt() {
        List<EventHandler<String>> handlers = List.of(handler1, handler2);
        service = new RetryableEventProcessingService<>(handlers, handlerRetry, retryCountStore,
                                                        handlersExecutionStrategy, dlqMessageSender, maxRetryCount,
                                                        dlqTopicName);

        when(retryCountStore.getRetryCount(eventKey)).thenReturn(INT_VALUE_0);

        service.process(eventKey, handlers, event, acknowledgment);

        verify(acknowledgment).acknowledge();
        verify(handlersExecutionStrategy).processHandlers(handlers, event);
        verify(retryCountStore, never()).incrementRetryCount(any());
        verify(dlqMessageSender, never()).send(any(), any(), anyLong(), any(), any(), any());
    }

    @Test
    void process_RetryAttemptSuccess() {
        List<EventHandler<String>> handlers = List.of(handler1, handler2);
        service = new RetryableEventProcessingService<>(handlers, handlerRetry, retryCountStore,
                                                        handlersExecutionStrategy, dlqMessageSender, maxRetryCount,
                                                        dlqTopicName);

        when(retryCountStore.getRetryCount(eventKey)).thenReturn(INT_VALUE_1);
        when(retryCountStore.getUnworkedHandlers(eventKey)).thenReturn(List.of(handler2));

        service.process(eventKey, handlers, event, acknowledgment);

        verify(acknowledgment).acknowledge();
        verify(handlersExecutionStrategy).processHandlers(List.of(handler2), event);
        verify(retryCountStore, never()).incrementRetryCount(any());
    }

    @Test
    void process_HandlerFailureIncrementsRetryCountThrowsException() {
        List<EventHandler<String>> handlers = List.of(handler1);
        service = new RetryableEventProcessingService<>(handlers, handlerRetry, retryCountStore,
                                                        handlersExecutionStrategy, dlqMessageSender, maxRetryCount,
                                                        dlqTopicName);

        when(retryCountStore.getRetryCount(eventKey)).thenReturn(INT_VALUE_0);

        HandlerException handlerException = new HandlerException("Error", handler1.getClass().getSimpleName());
        HandlersException handlersException = new HandlersException("Error", List.of(handlerException));

        doThrow(handlersException).when(handlersExecutionStrategy).processHandlers(anyList(), eq(event));

        when(retryCountStore.getRetryCount(eventKey)).thenReturn(INT_VALUE_0).thenReturn(INT_VALUE_1);

        assertThrows(HandlersException.class, () -> service.process(eventKey, handlers, event, acknowledgment));

        verify(acknowledgment).acknowledge();
        verify(retryCountStore).incrementRetryCount(eventKey);
        verify(retryCountStore).setUnworkedHandlers(eq(eventKey), anyList());
        verify(dlqMessageSender, never()).send(any(), any(), anyLong(), any(), any(), any());
    }

    @Test
    void process_MaxRetriesReachedSendsToDlq() {
        List<EventHandler<String>> handlers = List.of(handler1);
        service = new RetryableEventProcessingService<>(handlers, handlerRetry, retryCountStore,
                                                        handlersExecutionStrategy, dlqMessageSender, maxRetryCount,
                                                        dlqTopicName);


        String handlerName = handler1.getClass().getSimpleName();
        HandlerException handlerException = new HandlerException(handlerName, "Fatal Error");
        HandlersException handlersException = new HandlersException("Fatal Error", List.of(handlerException));


        doThrow(handlersException).when(handlersExecutionStrategy).processHandlers(anyList(), eq(event));
        when(retryCountStore.getRetryCount(eventKey)).thenReturn(maxRetryCount - 1).thenReturn(maxRetryCount);

        service.process(eventKey, handlers, event, acknowledgment);

        verify(retryCountStore).incrementRetryCount(eventKey);
        verify(retryCountStore).setUnworkedHandlers(eq(eventKey), anyList());

        verify(dlqMessageSender).send(eq(event), argThat(set -> set.contains(handlerName)),
                                      eq(LONG_VALUE_3), errorMessagesCaptor.capture(), eq(eventKey), eq(dlqTopicName));

        Map<String, String> capturedErrors = errorMessagesCaptor.getValue();
        assertEquals("Fatal Error", capturedErrors.get(handlerName));

        verify(retryCountStore).clearRetryState(eventKey);
    }
}
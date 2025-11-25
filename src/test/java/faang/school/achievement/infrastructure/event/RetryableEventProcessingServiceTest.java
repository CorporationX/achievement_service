package faang.school.achievement.infrastructure.event;

import faang.school.achievement.handlers.EventHandler;
import faang.school.achievement.infrastructure.kafka.HandlerRetry;
import faang.school.achievement.infrastructure.store.RetryCountStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetryableEventProcessingServiceTest {

    private final String eventKey = "123";
    private final Object event = new Object();
    private final long maxRetries = 3L;
    private final String dlqTopic = "dlq-topic";
    @Mock
    private HandlerRetry handlerRetry;
    @Mock
    private RetryCountStore<Object, EventHandler<Object>> retryCountStore;
    @Mock
    private Acknowledgment ack;
    @InjectMocks
    private RetryableEventProcessingService<Object, EventHandler<Object>> service;
    @Mock
    private EventHandler<Object> handler1;
    @Mock
    private EventHandler<Object> handler2;
    private List<EventHandler<Object>> allHandlers;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "countRetryableTopic", maxRetries);
        ReflectionTestUtils.setField(service, "topicDlqName", dlqTopic);

        allHandlers = List.of(handler1, handler2);
        ReflectionTestUtils.setField(service, "handlers", allHandlers);
    }

    @Test
    void process_FirstAttemptSuccess() {
        when(retryCountStore.getRetryCount(eventKey)).thenReturn(0);

        service.process(eventKey, allHandlers, event, ack);

        verify(ack).acknowledge();
        verify(handlerRetry).execute(any());
    }

    @Test
    void process_RetryAttemptSuccess() {
        when(retryCountStore.getRetryCount(eventKey)).thenReturn(1);
        when(retryCountStore.getUnworkedHandlers(eventKey)).thenReturn(List.of(handler2));

        service.process(eventKey, allHandlers, event, ack);

        verify(ack).acknowledge();
        verify(handlerRetry).execute(any());
    }
}
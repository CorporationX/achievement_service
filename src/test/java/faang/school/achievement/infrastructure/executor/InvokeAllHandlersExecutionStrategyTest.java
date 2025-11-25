package faang.school.achievement.infrastructure.executor;

import faang.school.achievement.exception.HandlerException;
import faang.school.achievement.exception.HandlersException;
import faang.school.achievement.handlers.TimedEventHandler;
import faang.school.achievement.service.DataForTestsObtainAchievements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvokeAllHandlersExecutionStrategyTest extends DataForTestsObtainAchievements {

    private final Object event = new Object();
    @Mock
    private ThreadPoolTaskExecutor taskExecutor;
    @Mock
    private ThreadPoolExecutor threadPoolExecutor;
    @InjectMocks
    private InvokeAllHandlersExecutionStrategy<Object> strategy;
    @Mock
    private TimedEventHandler<Object> handler1;
    @Mock
    private TimedEventHandler<Object> handler2;
    @Mock
    private Future<Void> future1;
    @Mock
    private Future<Void> future2;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(strategy, "handlerDefaultCompletionTime", HANDLER_EXECUTION_TIME);
        when(taskExecutor.getThreadPoolExecutor()).thenReturn(threadPoolExecutor);
    }

    @Test
    void processHandlers_SuccessfulExecution() throws InterruptedException, ExecutionException {

        List<Future<Void>> futures = List.of(future1, future2);

        when(threadPoolExecutor.invokeAll(anyCollection(), anyLong(), any(TimeUnit.class))).thenReturn((List) futures);
        when(future1.isCancelled()).thenReturn(false);
        when(future2.isCancelled()).thenReturn(false);
        when(future1.get()).thenReturn(null);
        when(future2.get()).thenReturn(null);
        List<TimedEventHandler<Object>> handlers = List.of(handler1, handler2);
        strategy.processHandlers(handlers, event);
    }

    @Test
    void processHandlers_OneHandlerFailedWithHandlerException() throws InterruptedException, ExecutionException {
        List<TimedEventHandler<Object>> handlers = List.of(handler1);
        List<Future<Void>> futures = List.of(future1);

        when(threadPoolExecutor.invokeAll(anyCollection(), anyLong(), any(TimeUnit.class))).thenReturn((List) futures);
        when(future1.isCancelled()).thenReturn(false);

        HandlerException handlerException = new HandlerException("Handler1", "TestError");
        ExecutionException executionException = new ExecutionException(handlerException);
        when(future1.get()).thenThrow(executionException);

        HandlersException exception = assertThrows(HandlersException.class,
                                                   () -> strategy.processHandlers(handlers, event));

        assertEquals(1, exception.getHandlerExceptions().size());
        assertEquals("TestError", exception.getHandlerExceptions().get(0).getMessage());
    }

    @Test
    void processHandlers_OneHandlerTimedOutIsCancelled() throws InterruptedException {
        List<TimedEventHandler<Object>> handlers = List.of(handler1);
        List<Future<Void>> futures = List.of(future1);

        when(threadPoolExecutor.invokeAll(anyCollection(), anyLong(), any(TimeUnit.class))).thenReturn((List) futures);
        when(future1.isCancelled()).thenReturn(true);

        HandlersException exception = assertThrows(HandlersException.class,
                                                   () -> strategy.processHandlers(handlers, event));

        assertEquals(1, exception.getHandlerExceptions().size());
        assertEquals("Handler execution timeout", exception.getHandlerExceptions().get(0).getMessage());
    }

    @Test
    void processHandlers_ExecutorServiceInterrupted() throws InterruptedException {
        List<TimedEventHandler<Object>> handlers = List.of(handler1);

        when(threadPoolExecutor.invokeAll(anyCollection(), anyLong(), any(TimeUnit.class))).thenThrow(
            new InterruptedException());

        HandlersException exception = assertThrows(HandlersException.class,
                                                   () -> strategy.processHandlers(handlers, event));

        assertEquals("Handlers execution interrupted", exception.getMessage());
    }

    @Test
    void processHandlers_HandlerLogicWrapsException() throws Exception {
        List<TimedEventHandler<Object>> handlers = List.of(handler1);
        doAnswer(invocation -> {
            Collection<Callable<Void>> tasks = invocation.getArgument(0);
            Callable<Void> task = tasks.iterator().next();

            try {
                task.call();
                return List.of(future1);
            } catch (Exception e) {
                Future<Void> failedFuture = mock(Future.class);
                when(failedFuture.get()).thenThrow(new ExecutionException(e));
                return List.of(failedFuture);
            }
        }).when(threadPoolExecutor).invokeAll(anyCollection(), anyLong(), any(TimeUnit.class));

        RuntimeException rootCause = new RuntimeException("Root cause");
        doThrow(rootCause).when(handler1).handle(event);

        HandlersException ex = assertThrows(HandlersException.class, () -> strategy.processHandlers(handlers, event));

        assertEquals(1, ex.getHandlerExceptions().size());
        HandlerException handlerEx = ex.getHandlerExceptions().get(0);

        assertEquals(rootCause, handlerEx.getCause());
    }
}
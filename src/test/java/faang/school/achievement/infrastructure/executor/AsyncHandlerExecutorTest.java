package faang.school.achievement.infrastructure.executor;

import faang.school.achievement.exception.HandlerException;
import faang.school.achievement.handlers.TimedEventHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AsyncHandlerExecutorTest {

    @Mock
    private TimedEventHandler<Object> handler;

    @InjectMocks
    private AsyncHandlerExecutor asyncHandlerExecutor;

    private final Object event = new Object();

    @Test
    void execute_ShouldCallHandlerSuccessfully() {
        asyncHandlerExecutor.execute(handler, event);

        verify(handler).handle(event);
    }

    @Test
    void execute_ShouldCatchHandlerExceptionAndLogWarn() {
        HandlerException exception = new HandlerException("Expected error", "TestHandler");
        doThrow(exception).when(handler).handle(event);
        asyncHandlerExecutor.execute(handler, event);
        verify(handler).handle(event);
    }

    @Test
    void execute_ShouldCatchUnexpectedExceptionAndLogError() {
        RuntimeException exception = new RuntimeException("Unexpected error");
        doThrow(exception).when(handler).handle(event);
        asyncHandlerExecutor.execute(handler, event);
        verify(handler).handle(event);
    }
}
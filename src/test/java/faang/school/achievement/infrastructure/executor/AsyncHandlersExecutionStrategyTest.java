package faang.school.achievement.infrastructure.executor;

import faang.school.achievement.handlers.EventHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class AsyncHandlersExecutionStrategyTest {

    private final Object event = new Object();
    @Mock
    private AsyncHandlerExecutor asyncHandlerExecutor;
    @InjectMocks
    private AsyncHandlersExecutionStrategy<Object> strategy;
    @Mock
    private EventHandler<Object> handler1;
    @Mock
    private EventHandler<Object> handler2;

    @Test
    void processHandlers_ShouldExecuteAllHandlersAsync() {
        List<EventHandler<Object>> handlers = List.of(handler1, handler2);
        strategy.processHandlers(handlers, event);
        verify(asyncHandlerExecutor).execute(handler1, event);
        verify(asyncHandlerExecutor).execute(handler2, event);
        verifyNoMoreInteractions(asyncHandlerExecutor);
    }

    @Test
    void processHandlers_WithEmptyListShouldDoNothing() {
        List<EventHandler<Object>> handlers = List.of();
        strategy.processHandlers(handlers, event);
        verifyNoMoreInteractions(asyncHandlerExecutor);
    }
}
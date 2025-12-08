package faang.school.achievement.infrastructure.executor;

import faang.school.achievement.handlers.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("asyncHandlersExecutionStrategy")
@RequiredArgsConstructor
public class AsyncHandlersExecutionStrategy<T> implements HandlersExecutionStrategy<T, EventHandler<T>> {
    private final AsyncHandlerExecutor asyncHandlerExecutor;

    @Override
    public void processHandlers(List<EventHandler<T>> handlers, T event) {
        for (EventHandler<T> handler : handlers) {
            asyncHandlerExecutor.execute(handler, event);
        }
    }
}
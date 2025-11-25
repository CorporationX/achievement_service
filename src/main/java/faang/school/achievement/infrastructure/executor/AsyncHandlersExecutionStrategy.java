package faang.school.achievement.infrastructure.executor;

import faang.school.achievement.handlers.TimedEventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("asyncHandlersExecutionStrategy")
@RequiredArgsConstructor
public class AsyncHandlersExecutionStrategy<T> implements HandlersExecutionStrategy<T, TimedEventHandler<T>> {
    private final AsyncHandlerExecutor asyncHandlerExecutor;

    @Override
    public void processHandlers(List<TimedEventHandler<T>> handlers, T event) {
        for (TimedEventHandler<T> handler : handlers) {
            asyncHandlerExecutor.execute(handler, event);
        }
    }
}
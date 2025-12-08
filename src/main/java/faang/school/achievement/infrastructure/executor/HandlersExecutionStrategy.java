package faang.school.achievement.infrastructure.executor;

import faang.school.achievement.exception.HandlersException;
import faang.school.achievement.handlers.EventHandler;

import java.util.List;

public interface HandlersExecutionStrategy<T, H extends EventHandler<T>> {
    void processHandlers(List<H> handlers, T event) throws HandlersException;
}
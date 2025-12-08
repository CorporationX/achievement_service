package faang.school.achievement.infrastructure.executor;

import faang.school.achievement.exception.HandlerException;
import faang.school.achievement.handlers.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static faang.school.achievement.utils.Utils.getSimpleClassName;

@Slf4j
@Component
public class AsyncHandlerExecutor {
    @Async("handlersExecutor")
    public <T> void execute(EventHandler<T> handler, T event) {
        String name = getSimpleClassName(handler);
        try {
            handler.handle(event);
        } catch (HandlerException e) {
            log.warn("Async handler {} failed with HandlerException: {}", name, e.getMessage(), e);
        } catch (Exception e) {
            log.error("Async handler {} failed with unexpected error", name, e);
        }
    }
}
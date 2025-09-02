package faang.school.achievement.listener;

import faang.school.achievement.exception.HandlerNotFoundException;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.dto.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    private final List<EventHandler<T>> handlers;

    protected void handleEvent(T event) {
        handlers.stream()
                .filter(handler -> handler.getInstance().equals(event.getClass()))
                .findFirst()
                .orElseThrow(() -> new HandlerNotFoundException(ErrorType.HANDLER_NOT_FOUND))
                .handle(event);
    }
}

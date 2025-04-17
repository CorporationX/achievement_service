package faang.school.achievement.listener;

import faang.school.achievement.dto.event.EventDto;
import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventListener {
    private final EventHandler eventHandler;

    public void handleMessage(EventDto event) {
        log.debug("Received message: {}", event);
        eventHandler.handleEvent(event);
    }
}

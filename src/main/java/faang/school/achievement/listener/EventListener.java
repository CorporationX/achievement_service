package faang.school.achievement.listener;

import faang.school.achievement.dto.event.EventDto;
import faang.school.achievement.handler.AchievementHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventListener {
    private final List<AchievementHandler> achievementHandlers;

    public void handleMessage(EventDto event) {
        log.debug("Received message: {}", event);
        achievementHandlers.stream()
                .filter(achievementHandler -> achievementHandler.getChannel().equals(event.getChannel()))
                .findFirst()
                .ifPresent(achievementHandler -> achievementHandler.handleEvent(event));
    }
}

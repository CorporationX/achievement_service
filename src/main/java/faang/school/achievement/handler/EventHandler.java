package faang.school.achievement.handler;

import faang.school.achievement.dto.event.AchievementEventDto;
import faang.school.achievement.sender.Sender;
import faang.school.achievement.thread_pool.EventThreadPools;
import faang.school.achievement.dto.event.EventDto;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventHandler {
    private final AchievementService achievementService;
    private final EventThreadPools eventThreadPools;
    private final Sender sender;

    public CompletableFuture<Void> handleEvent(EventDto event) {
        return CompletableFuture.runAsync(() -> {
            log.debug("start handle event {}", event);
            achievementService.getAchievementByEventType(event.getEventType()).stream()
                    .filter(achievement -> !achievementService.hasUserAchievement(event.getAuthorId(), achievement.getId()))
                    .forEach(achievement -> {
                        if (achievementService.incrementAndCheckProgress(event.getAuthorId(), achievement.getId())) {
                            achievementService.saveAchievementToUser(event.getAuthorId(), achievement);
                            log.debug("is achieved progress achievement with id {}", achievement.getId());
                            sender.send(AchievementEventDto.builder()
                                    .userId(event.getAuthorId())
                                    .title(achievement.getTitle())
                                    .description(achievement.getDescription())
                                    .build());
                        }
                    });
        }, eventThreadPools.getThreadPoolFor(event.getEventType()));
    }
}

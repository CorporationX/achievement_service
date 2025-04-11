package faang.school.achievement.handler;

import faang.school.achievement.dto.event.EventDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.EventType;
import faang.school.achievement.service.AchievementService;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;


@Slf4j
public abstract class AchievementHandler {
    private final AchievementService achievementService;
    private final EventType eventType;

    protected AchievementHandler(AchievementService achievementService, EventType eventType) {
        this.achievementService = achievementService;
        this.eventType = eventType;
    }

    public abstract String getChannel();

    public void handleEvent(EventDto event) {
        log.debug("start handle event {}", event.toString());
        achievementService.getAchievementByEvent(eventType).stream()
                .sorted(Comparator.comparingLong(Achievement::getGoal))
                .filter(achievement ->
                        !achievementService.hasUserAchievement(event.getAuthorId(), achievement.getId()))
                .findFirst()
                .ifPresent(achievement -> {
                    if (achievementService
                            .incrementAndCheckAchievementProgress(event.getAuthorId(), achievement.getId())) {
                        achievementService.saveAchievementToUser(event.getAuthorId(), achievement);
                        log.debug("is achieved progress achievement with id {}", achievement.getId());
                        // notify logic
                    }
                });
    }
}

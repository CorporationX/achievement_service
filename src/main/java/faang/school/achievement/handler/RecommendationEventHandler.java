package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.redis.event.RecommendationEvent;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
@Slf4j
public abstract class RecommendationEventHandler implements EventHandler<RecommendationEvent> {

    private final String achievementName;
    private final AchievementCache achievementCache;
    private final AchievementService achievementService;

    @Override
    @Async
    public void handleEvent(RecommendationEvent event) {
        Achievement achievement = achievementCache.get(achievementName);
        long userId = event.getReceiverId();
        long achievementId = achievement.getId();
        if (!achievementService.userHasAchievement(userId, achievementId)) {
            log.info("Achievement progress for user with ID = {} for achievement {} was created",
                    userId, achievementName);
            achievementService.createProgressIfNecessary(userId, achievementId);
            AchievementProgress achievementProgress = achievementService.getProgress(userId, achievementId);
            achievementProgress.increment();
            long currentPoints = achievementProgress.getCurrentPoints();
            log.info("Current points of achievement progress for achievement {} for user with ID = {} are {}",
                    achievementName, userId, achievementProgress.getCurrentPoints());
            if (currentPoints == achievement.getPoints()) {
                achievementService.giveAchievement(userId, achievement);
                log.info("User with ID = {} was received achievement {}", userId, achievementName);
            }
        }
    }
}

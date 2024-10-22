package faang.school.achievement.handler;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventHandler<T> implements EventHandler<T> {
    private final AchievementService achievementService;

    protected Achievement getAchievementByTitle(String title) {
        return achievementService.findByTitle(title);
    }

    protected void processAchievement(long userId, long achievementId) {
        if (achievementService.isUserHasAchievement(userId, achievementId)) {
            log.info("User {} already has achievement {}", userId, achievementId);
        } else {
            AchievementProgress achievementProgress = achievementService.createProgressIfNecessaryAndReturn(userId, achievementId);
            achievementService.incrementProgress(achievementProgress);
        }
    }
}

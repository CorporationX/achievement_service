package faang.school.achievement.handler;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractAchievementHandler<T> implements EventHandler<T> {

    private final AchievementService achievementService;
    private final AchievementCache achievementCache;

    @Async("achievementHandlingExecutor")
    public void handleAchievement(long userId, String achievementTitle) {
        log.info("Handling achievement titled '{}'", achievementTitle);
        Achievement achievement = achievementCache.get(achievementTitle);
        if (achievementService.hasAchievement(userId, achievement.getId())) {
            log.info("User with ID={} already has achievement titled '{}'", userId, achievementTitle);
            return;
        }
        handleProgress(userId, achievement);
    }

    private void handleProgress(long userId, Achievement achievement) {
        achievementService.createProgressIfNecessary(userId, achievement.getId());
        AchievementProgress achievementProgress = achievementService.getProgress(userId, achievement.getId());
        achievementProgress.increment();
        if (achievementProgress.getCurrentPoints() == achievement.getPoints()) {
            achievementService.giveAchievement(userId, achievement);
        }
        achievementService.updateProgress(achievementProgress);
    }
}

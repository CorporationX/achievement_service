package faang.school.achievement.handler;

import faang.school.achievement.exception.HandleAchievementException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.interfaces.Cache;
import faang.school.achievement.service.interfaces.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventHandler<T> implements EventHandler<T> {
    private final Cache<Achievement> achievementCache;
    private final AchievementService achievementService;

    @Async("eventHandlingTaskExecutor")
    public void handleAchievement(Long userId, String achievementTitle) {
        try {
            log.info("{}", achievementTitle);
            Achievement achievement = achievementCache.get(achievementTitle);
            handleAchievementProgress(userId, achievement);
        } catch (Exception ex) {
            String exceptionMessage = String.format("Error handling achievement for user id: %d", userId);
            HandleAchievementException e = new HandleAchievementException(exceptionMessage, ex);
            log.error(exceptionMessage, e);
            throw e;
        }
    }

    public void handleAchievementProgress(Long userId, Achievement achievement) {
        if (achievementService.hasAchievement(userId, achievement.getId())) {
            log.info("User with id: {} already has achievement with id:{}", userId, achievement.getId());
            return;
        }

        achievementService.createProgressIfNecessary(userId, achievement.getId());
        AchievementProgress progress = achievementService.getProgress(userId, achievement.getId());
        progress.increment();

        if (progress.getCurrentPoints() >= achievement.getPoints()) {
            achievementService.giveAchievement(userId, achievement.getId());
            log.info("User with id: {} received achievement with id: {}", userId, achievement.getId());
        }

        achievementService.updateProgress(progress);
    }

}

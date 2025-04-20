package faang.school.achievement.handler;

import faang.school.achievement.exception.HandleAchievementException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.interfaces.AchievementService;
import faang.school.achievement.service.interfaces.Cache;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventHandler<T> implements EventHandler<T> {
    private final Cache<Achievement> achievementCache;
    private final AchievementService achievementService;

    @Async("eventHandlingTaskExecutor")
    public void handleAchievement(Long userId, String achievementTitle) {
        log.info("Processing achievement: {}", achievementTitle);
        if (userId == null || achievementTitle == null) {
            throw new HandleAchievementException(
                    String.format("Invalid input: userId=%s, achievementTitle=%s", userId, achievementTitle));
        }
        Achievement achievement = achievementCache.get(achievementTitle);
        handleAchievementProgress(userId, achievement);
    }

    public void handleAchievementProgress(Long userId, Achievement achievement) {
        if (userId == null || achievement == null) {
            throw new HandleAchievementException(
                    String.format("Invalid input in handleAchievementProgress: userId=%s, achievement=%s",
                            userId, achievement));
        }
        try {
            if (achievementService.hasAchievement(userId, achievement.getId())) {
                log.info("User with id: {} already has achievement with id:{}", userId, achievement.getId());
                return;
            }
            achievementService.createProgressIfNecessary(userId, achievement.getId());
            AchievementProgress progress = achievementService.getProgress(userId, achievement.getId());
            if (progress == null) {
                throw new HandleAchievementException(
                        String.format("AchievementProgress not found for userId=%d, achievementId=%d",
                                userId, achievement.getId()));
            }
            progress.increment();
            if (progress.getCurrentPoints() >= achievement.getPoints()) {
                achievementService.giveAchievement(userId, achievement.getId());
                log.info("User with id: {} received achievement with id: {}", userId, achievement.getId());
            }
            achievementService.updateProgress(progress);
        } catch (EntityNotFoundException ex) {
            throw new HandleAchievementException(
                    String.format("Entity not found during achievement progress handling: %s", ex.getMessage()), ex);
        } catch (DataAccessException ex) {
            throw new HandleAchievementException(
                    String.format("Database error during achievement progress handling: %s", ex.getMessage()), ex);
        } catch (Exception ex) {
            throw new HandleAchievementException(
                    String.format("Unexpected error while processing achievement progress: %s", ex.getMessage()), ex);
        }
    }
}

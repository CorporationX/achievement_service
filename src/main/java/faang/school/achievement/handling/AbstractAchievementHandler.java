package faang.school.achievement.handling;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;

@Slf4j
public abstract class AbstractAchievementHandler<T> implements EventHandler<T> {
    protected final AchievementService achievementService;
    protected final AchievementCache cachedAchievements;
    protected final String ACHIEVEMENT_CODE;

    protected AbstractAchievementHandler(
            AchievementService achievementService,
            AchievementCache cachedAchievements,
            String achievementCode) {
        this.achievementService = achievementService;
        this.cachedAchievements = cachedAchievements;
        this.ACHIEVEMENT_CODE = achievementCode;
    }

    @Async("achievementExecutor")
    @Retryable(
            retryFor = {OptimisticLockException.class, ObjectOptimisticLockingFailureException.class},
            maxAttemptsExpression = "${achievement.retry.max-attempts}",
            backoff = @Backoff(delayExpression = "${achievement.retry.delay}")
    )
    @Transactional
    @Override
    public void handleEvent(T event) {
        Achievement achievement = cachedAchievements.getOrThrow(ACHIEVEMENT_CODE);
        long achievementId = achievement.getId();
        long userId = extractUserId(event);

        if (!achievementService.hasAchievement(userId, achievementId)) {
            log.info("Считаем прогресс...");
            achievementService.createProgressIfNecessary(userId, achievementId);

            AchievementProgress progress = achievementService.getProgress(userId, achievementId);
            progress.increment();
            log.info("Прогресс засчитан успешно.");

            if (progress.getCurrentPoints() >= achievement.getPoints()) {
                achievementService.giveAchievement(userId, achievementId);
                log.info("Достижение было выдано успешно.");
            }
        }
    }

    protected abstract long extractUserId(T event);
}
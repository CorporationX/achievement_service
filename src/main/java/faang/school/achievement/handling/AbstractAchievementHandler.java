package faang.school.achievement.handling;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import jakarta.persistence.OptimisticLockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
public abstract class AbstractAchievementHandler<T> implements AchievementHandler<T> {

    protected final AchievementService achievementService;
    protected final AchievementCache cachedAchievements;
    protected final String achievementCode;
    private final TransactionTemplate template;

    protected AbstractAchievementHandler(
            AchievementService achievementService,
            AchievementCache cachedAchievements,
            PlatformTransactionManager manager,
            String achievementCode
    ) {
        this.achievementService = achievementService;
        this.cachedAchievements = cachedAchievements;
        this.achievementCode = achievementCode;
        this.template = new TransactionTemplate(manager);
    }

    @Async("achievementExecutor")
    @Retryable(
            retryFor = {
                    OptimisticLockException.class,
                    ObjectOptimisticLockingFailureException.class
            },
            maxAttemptsExpression = "${achievement.retry.max-attempts}",
            backoff = @Backoff(delayExpression = "${achievement.retry.delay}")
    )
    public void handle(T event) {
        template.executeWithoutResult(status -> {
            try {
                handleAchievement(event);
            } catch (RuntimeException e) {
                status.setRollbackOnly();
                throw e;
            }
        });
    }

    @Override
    public void handleAchievement(T event) {
        Achievement achievement = cachedAchievements.getOrThrow(achievementCode);
        long achievementId = achievement.getId();
        long userId = extractUserId(event);

        if (!achievementService.hasAchievement(userId, achievementId)) {
            log.debug("Counting progress...");
            achievementService.createProgressIfNecessary(userId, achievementId);

            AchievementProgress progress = achievementService.getProgress(userId, achievementId);
            progress.increment();
            log.debug("The progress was successfully increased");

            if (progress.getCurrentPoints() >= achievement.getPoints()) {
                achievementService.assignAchievementToUser(userId, achievementId);
                log.debug("The achievement was successfully assigned to the user");
            }
        }
    }

    protected abstract long extractUserId(T event);
}
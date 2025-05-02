package faang.school.achievement.service.achievementprogress;

import faang.school.achievement.model.AchievementProgress;
import jakarta.persistence.OptimisticLockException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Transactional;

public interface AchievementProgressService {

    @Transactional
    void createProgressIfNecessary(long userId, long achievementId);

    @Transactional
    AchievementProgress getProgress(long userId, long achievementId);

    @Transactional
    @Retryable(
            value = {OptimisticLockException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 100)
    )
    AchievementProgress progressIncrement(long id);
}

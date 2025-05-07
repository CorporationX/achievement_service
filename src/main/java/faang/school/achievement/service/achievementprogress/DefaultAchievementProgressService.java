package faang.school.achievement.service.achievementprogress;

import faang.school.achievement.exception.AchievementProgressNotFoundException;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementProgressRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "app.service.achievement-progress",
        havingValue = "default",
        matchIfMissing = true
)
public class DefaultAchievementProgressService implements AchievementProgressService {

    private final AchievementProgressRepository achievementProgressRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void createProgressIfNecessary(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    @Transactional(readOnly = true)
    @Override
    public AchievementProgress getProgress(long userId, long achievementId) {
        log.debug("Starting obtain achievement progress for user {}...", userId);
        return achievementProgressRepository
                .findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new AchievementProgressNotFoundException("Achievement progress not found"));
    }

    @Transactional
    @Retryable(
            value = {OptimisticLockException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 100)
    )
    @Override
    public AchievementProgress progressIncrement(long id) {
        log.info("Starting progressIncrement for id: {}", id);
        AchievementProgress progress = entityManager.find(AchievementProgress.class, id);

        if (progress == null) {
            throw new EntityNotFoundException(String.format("Achievement progress with id %d not found", id));
        }

        progress.increment();

        log.info("Progress incremented for id: {}", id);

        return progress;
    }
}

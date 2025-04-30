package faang.school.achievement.service.achievement_progress;

import faang.school.achievement.exception.NotFoundException;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementProgressRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementProgressServiceImpl implements AchievementProgressService {

    private final AchievementProgressRepository achievementProgressRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void createProgressIfNecessary(long userId, long achievementId) {
        log.info("Starting createProgressIfNecessary for userId: {} and achievementId: {}", userId, achievementId);
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
        log.info("Completed createProgressIfNecessary for userId: {} and achievementId: {}", userId, achievementId);
    }

    @Transactional
    @Override
    public AchievementProgress getProgress(long userId, long achievementId) {
        log.info("Starting getProgress for userId: {} and achievementId: {}", userId, achievementId);

        AchievementProgress progress = achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> {
                    log.warn("AchievementProgress not found for userId: {} and achievementId: {}",
                            userId, achievementId);
                    return new NotFoundException(
                            String.format("AchievementProgress %d for the user %d was not found",
                                    achievementId, userId));
                });

        log.info("Completed getProgress for userId: {} and achievementId: {}", userId, achievementId);
        return progress;
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
            log.warn("Achievement progress not found for id: {}", id);
            throw new NotFoundException(String.format("Achievement progress with id %d not found", id));
        }

        progress.increment();
        log.info("Progress incremented");

        return progress;
    }
}
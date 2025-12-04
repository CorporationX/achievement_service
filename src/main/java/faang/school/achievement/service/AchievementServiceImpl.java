package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementServiceImpl implements AchievementService {

    @Value("${retry.optimistic-lock.max-attempts}")
    private int maxAttempts;
    @Value("${retry.optimistic-lock.delay}")
    private int delay;

    private final AchievementRepository achievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final UserAchievementRepository userAchievementRepository;

    @Override
    @Transactional
    public void operationAchievement(long userId, String achievementTitle) {
        Achievement achievement = getAchievementByTitle(achievementTitle);
        boolean hasAchievement = userAchievementRepository
                .existsByUserIdAndAchievementId(userId, achievement.getId());
        if (!hasAchievement) {
            achievementProgressRepository.createProgressIfNecessary(userId, achievement.getId());
            long currentProgress = incrementAndSaveProgress(userId, achievement.getId());
            if (currentProgress >= achievement.getPoints()) {
                giveAchievement(userId, achievement);
                log.info("User {} received achievement {}", userId, achievementTitle);
            }
        }
    }

    private void giveAchievement(Long userId, Achievement achievement) {

        UserAchievement userAchievement = UserAchievement.builder()
                .userId(userId)
                .achievement(achievement)
                .build();
        userAchievementRepository.save(userAchievement);
    }

    private Achievement getAchievementByTitle(String title) {
        return achievementRepository.findByTitle(title);
    }

    @Retryable(
            retryFor = OptimisticLockingFailureException.class,
            maxAttemptsExpression = "${retry.optimistic-lock.max-attempts}",
            backoff = @Backoff(delayExpression = "${retry.optimistic-lock.delay}")
    )
    private long incrementAndSaveProgress(long userId, Long achievementId) {
        Optional<AchievementProgress> progress = achievementProgressRepository
                .findByUserIdAndAchievementId(userId, achievementId);
        long currentPoints = 0;
        if (progress.isPresent()) {
            AchievementProgress achievementProgress = progress.get();
            achievementProgress.increment();
            currentPoints = achievementProgress.getCurrentPoints();
        }
        return currentPoints;
    }
}
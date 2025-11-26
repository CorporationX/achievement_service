package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AchievementService {

    @Value("${retry.optimistic-lock.max-attempts}")
    private int maxAttempts;
    @Value("${retry.optimistic-lock.delay}")
    private int delay;

    private final AchievementRepository achievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final UserAchievementRepository userAchievementRepository;

    public boolean hasAchievement(Long userId, Long achievementId) {
        List<UserAchievement> achievements = userAchievementRepository.findByUserId(userId);

        return achievements.stream().anyMatch(a -> achievementId.equals(a.getAchievement().getId()));
    }

    @Transactional
    public void createProgressIfNecessary(long userId, Long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    public long getProgress(long userId, Long achievementId) {
        Optional<AchievementProgress> progress = achievementProgressRepository
                .findByUserIdAndAchievementId(userId, achievementId);
        return progress.map(AchievementProgress::getCurrentPoints).orElse(0L);
    }

    @Transactional
    public void giveAchievement(Long userId, Achievement achievement) {
        UserAchievement userAchievement = UserAchievement.builder()
                .userId(userId)
                .achievement(achievement)
                .build();
        userAchievementRepository.save(userAchievement);
    }

    public Achievement getAchievementByTitle(String title) {
        return achievementRepository.findByTitle(title);
    }

    @Retryable(
            retryFor = OptimisticLockingFailureException.class,
            maxAttemptsExpression  = "${retry.optimistic-lock.max-attempts}",
            backoff = @Backoff(delayExpression = "${retry.optimistic-lock.delay}")
    )
    @Transactional
    public void incrementAndSaveProgress(long userId, Long achievementId) {
        achievementProgressRepository
                .findByUserIdAndAchievementId(userId, achievementId).ifPresent(AchievementProgress::increment);
    }
}

package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.EventType;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementService {
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementRepository achievementRepository;

    public List<Achievement> getAchievementByEvent(EventType eventType) {
        List<Achievement> achievements = achievementRepository.findByEvent(eventType);
        log.debug("getting achievements by event title {}", eventType);
        return achievements;
    }

    public boolean hasUserAchievement(long authorId, long achievementId) {
        boolean isAchieved = userAchievementRepository.existsByUserIdAndAchievementId(authorId, achievementId);
        log.debug("author with id {} is achieved achievement with id {}? {}", authorId, achievementId, isAchieved);
        return isAchieved;
    }

    @Retryable(
            retryFor = { OptimisticLockingFailureException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 100)
    )
    @Transactional
    public boolean incrementAndCheckAchievementProgress(long authorId, long achievementId) {
        AchievementProgress achievementProgress = getAchievementProgress(authorId, achievementId);
        log.debug("getting progress achievement with id {} from the user with id {} and achievement with id {}",
                achievementProgress.getId(), achievementProgress.getUserId(), achievementId);

        int updatedRows = achievementProgressRepository.incrementProgress(
                achievementProgress.getUserId(),
                achievementProgress.getAchievement().getId(),
                achievementProgress.getVersion());

        if (updatedRows == 0) {
            throw new OptimisticLockingFailureException("Version mismatch");
        }

        return achievementProgress.getAchievement().getGoal() == achievementProgressRepository.getCurrentPoints(
                achievementProgress.getUserId(),
                achievementProgress.getAchievement().getId());
    }

    public void saveAchievementToUser(long authorId, Achievement achievement) {
        UserAchievement userAchievement = userAchievementRepository.save(UserAchievement.builder()
                .userId(authorId)
                .achievement(achievement)
                .build());
        log.debug("save user achievement with id {} from the user with id {} and achievement with id {}",
                userAchievement.getUserId(), authorId, achievement.getId());
    }

    private AchievementProgress getAchievementProgress(long authorId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(authorId, achievementId);
        return achievementProgressRepository.findByUserIdAndAchievementId(authorId, achievementId);
    }
}
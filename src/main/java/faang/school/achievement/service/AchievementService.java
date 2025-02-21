package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final UserAchievementRepository userAchievementRepository;

    private final AchievementProgressRepository achievementProgressRepository;

    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional
    public void createProgressIfNecessary(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    public AchievementProgress getProgress(long userId, long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Achievement progress for achievement id = %d not found", achievementId)));
    }

    public void saveProgress(AchievementProgress progress) {
        achievementProgressRepository.save(progress);
    }

    public void saveUserAchievement(Achievement achievement, long userId) {
        userAchievementRepository.save(buildUserAchievement(achievement, userId));
    }

    private UserAchievement buildUserAchievement(Achievement achievement, long userId) {
        return UserAchievement.builder()
                .userId(userId)
                .achievement(achievement)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
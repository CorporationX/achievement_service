package faang.school.achievement.service;

import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;

    public Achievement getAchievementByTitle(String title) {
        return achievementRepository.findByTitleIgnoreCase(title).orElseThrow(
                () -> new RuntimeException("Achievement not found")
        );
    }

    public boolean hasAchievement(Long userId, Long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional
    public AchievementProgress createProgressIfNecessaryAndReturn(Long userId, Long achievementId) {
        achievementProgressRepository
                .createProgressIfNecessary(userId, achievementId);
        return achievementProgressRepository
                .findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new EntityNotFoundException("Achievement progress not found"));
    }

    public void giveAchievement(long achievementId, long userId) {
        var userAchievement = UserAchievement
                .builder()
                .achievement(Achievement.builder().id(achievementId).build())
                .userId(userId)
                .build();
        userAchievementRepository.save(userAchievement);
    }

    public void saveProgress(AchievementProgress progress) {
        achievementProgressRepository.save(progress);
    }
}

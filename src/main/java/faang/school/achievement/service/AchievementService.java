package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;

    @Cacheable(value = "achievements", key = "#title")
    public Achievement getAchievement(String title) {
        return achievementRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Achievement not found: " + title));
    }

    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional
    public void createProgressIfNecessary(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    public AchievementProgress getProgress(long userId, long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new RuntimeException("Progress not found for user " + userId +
                        " and achievement " + achievementId));
    }

    @Transactional
    public void incrementProgress(AchievementProgress progress) {
        progress.setCurrentPoints(progress.getCurrentPoints() + 1);
        achievementProgressRepository.save(progress);
    }

    @Transactional
    public void giveAchievement(long userId, long achievementId) {
        if (!hasAchievement(userId, achievementId)) {
            UserAchievement userAchievement = UserAchievement.builder()
                    .userId(userId)
                    .achievement(achievementRepository.findById(achievementId)
                            .orElseThrow(() -> new RuntimeException("Achievement not found: " + achievementId)))
                    .build();
            userAchievementRepository.save(userAchievement);
        }
    }
}
package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;

import java.util.Optional;

public interface AchievementService {
    boolean hasAchievement(Long userId, Long achievementId);

    AchievementProgress getOrCreateProgress(Long userId, Long achievementId);

    void giveAchievement(Long userId, Long achievementId);

    void updateProgress(AchievementProgress progress);

    Achievement getAchievementByTitle(String title);

    AchievementProgress createProgressIfNecessary(Long userId, Long achievementId);

    Optional<AchievementProgress> getProgress(Long userId, Long achievementId);
}

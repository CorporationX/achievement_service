package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;

public interface AchievementService {
    boolean hasAchievement(Long userId, Long achievementId);

    AchievementProgress getOrCreateProgress(Long userId, Long achievementId);

    void giveAchievement(Long userId, Long achievementId);

    void giveAchievement(AchievementProgress achievementProgress);

    void updateProgress(AchievementProgress progress);

    AchievementProgress getProgress(Long userId, Long achievementId);

    Achievement getAchievementFindByTitle(String title);

    Achievement getAchievementByTitle(String title);

    void createProgressIfNecessary(Long userId, Long achievementId);

    long incrementProgress(AchievementProgress progress);
}

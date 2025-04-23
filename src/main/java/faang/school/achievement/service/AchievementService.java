package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;

public interface AchievementService {
    boolean hasAchievement(Long userId, Long achievementId);
    void createProgressIfNecessary(Long userId, Long achievementId);
    AchievementProgress getProgress(Long userId, Long achievementId);
    void giveAchievement(AchievementProgress achievementProgress);
    Achievement getAchievementByTitle(String title);
    long incrementProgress(AchievementProgress progress);
}

package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;

public interface AchievementService {

    boolean hasAchievement(long userId, long achievementId);

    void createProgressIfNecessary(long userId, long achievementId);

    AchievementProgress getProgress(long userId, long achievementId);

    void giveAchievement(long userId, Achievement achievement);

    AchievementProgress saveAchievementProgress(AchievementProgress achievementProgress);
}

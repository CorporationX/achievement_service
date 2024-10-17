package faang.school.achievement.service;

import faang.school.achievement.model.entity.AchievementProgress;

public interface AchievementService {

    boolean hasAchievement(long userId, long achievementId);

    void createProgress(long userId, long achievementId);

    AchievementProgress getProgress(long userId, long achievementId);

    void giveAchievement(long userId, long achievementId);

    void saveProgress(AchievementProgress progress);
}

package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;

public interface AchievementService {

    boolean hasAchievement(Long userId, Achievement achievement);

    Achievement getAchievement(String title);

    AchievementProgress createProgressIfNecessary(Long userId, Achievement achievement);

    AchievementProgress increaseAchievementProgress(AchievementProgress achievementProgress, long points);

    UserAchievement giveAchievement(Long userId, Achievement achievement);

    AchievementProgress getProgress(Long userId, Long achievementId);
}

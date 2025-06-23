package faang.school.achievement.service;

import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;

import java.util.Optional;

public interface AchievementService {

    boolean hasAchievement(long userId, long achievementId);

    void createProgressIfNecessary(long userId, long achievementId);

    Optional<AchievementProgress> getProgress(long userId, long achievementId);

    void giveAchievement(UserAchievement userAchievement);
}

package faang.school.achievement.service.achievementprogress;

import faang.school.achievement.model.AchievementProgress;

public interface AchievementProgressService {

    void createProgressIfNecessary(long userId, long achievementId);

    AchievementProgress getProgress(long userId, long achievementId);

    AchievementProgress progressIncrement(long id);
}

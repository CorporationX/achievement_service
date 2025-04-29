package faang.school.achievement.service.userachievement;

import faang.school.achievement.model.Achievement;

public interface UserAchievementService {

    boolean hasAchievement(long userId, long achievementId);

    void giveAchievement(long userId, Achievement achievement);
}

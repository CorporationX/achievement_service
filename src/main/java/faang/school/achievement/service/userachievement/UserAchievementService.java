package faang.school.achievement.service.userachievement;

public interface UserAchievementService {

    boolean hasAchievement(long userId, long achievementId);

    void assignAchievementToUser(long userId, long achievementId);
}

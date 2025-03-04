package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;


public interface AchievementService {
    boolean hasAchievement(long userId, long achievementId);

    void createProgressIfNecessary(long userId, long achievementId);

    void giveAchievement(long userId, long achievementId);

    AchievementDto getAchievement(long achievementId);
    AchievementDto getAchievement(String achievementTitle);

    long incrementProgress(long userId, long achievementId) ;
}

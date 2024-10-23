package faang.school.achievement.service.achievement;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;

import java.util.List;

public interface AchievementService {
    AchievementProgress getProgress(long userId, long achievementId);

    void giveAchievement(long userId, Achievement achievement);

    boolean hasAchievement(long userId, long achievementId);

    void createProgressIfNecessary(long userId, long achievementId);

    List<AchievementDto> getAchievements(AchievementFilterDto filter);

    List<AchievementDto> getAchievementsBy(long userId);

    AchievementDto getAchievement(long achievementId);

    List<AchievementDto> getNotReceivedAchievements(long userId);
}

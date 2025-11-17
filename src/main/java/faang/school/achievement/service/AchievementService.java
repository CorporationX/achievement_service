package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.model.AchievementProgress;

import java.util.List;

public interface AchievementService {
    List<AchievementDto> getFilteredAchievements(AchievementFilterDto filterDto);

    List<AchievementDto> getUserAchievements(Long userId);

    AchievementDto getAchievementById(Long achievementId);

    List<AchievementProgressDto> getUnearnedAchievementsWithProgress(Long userId);

    boolean hasAchievement(long userId, long achievementId);

    void createProgressIfNecessary(long userId, long achievementId);

    AchievementProgress getProgress(long userId, long achievementId);

    void updateProgress(AchievementProgress progress);

    void giveAchievement(long userId, long achievementId);
}
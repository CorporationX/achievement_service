package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.model.UserAchievement;

import java.util.List;

public interface AchievementService {
    List<AchievementDto> getAllAchievements();
    List<UserAchievementDto> getAllUserAchievements(Long userId);
    AchievementDto getAchievement(Long achievementId);
    List<AchievementProgressDto> getUserAchievementProgress(Long userId);
}

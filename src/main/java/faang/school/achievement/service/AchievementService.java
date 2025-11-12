package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementProgressDto;

import java.util.List;

public interface AchievementService {
    List<AchievementDto> getFilteredAchievements(String title, String description, String rarity);

    List<AchievementDto> getUserAchievements(Long userId);

    AchievementDto getAchievementById(Long achievementId);

    List<AchievementProgressDto> getUnearnedAchievementsWithProgress(Long userId);
}
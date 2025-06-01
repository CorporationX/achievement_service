package faang.school.achievement.service.interfaces;

import faang.school.achievement.dto.AchievementDto;

import java.util.List;
import java.util.Map;


public interface AchievementRedisService {

    void saveAchievement(Map<String, AchievementDto> achievementDto);

    AchievementDto getAchievement(String title);

    List<AchievementDto> getAllAchievements();

    void cleanAchievements();

    boolean existsByTitle(String title);
}

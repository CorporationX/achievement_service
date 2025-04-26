package faang.school.achievement.service.interfaces;

import faang.school.achievement.model.Achievement;

import java.util.List;
import java.util.Map;


public interface AchievementRedisService {

    void saveAchievement(Map<String, Achievement> achievement);

    Achievement getAchievement(String title);

    List<Achievement> getAllAchievements();

    void cleanAchievements();

}

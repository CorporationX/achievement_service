package faang.school.achievement.service.cache;

import faang.school.achievement.model.Achievement;

public interface AchievementCacheService {
    Achievement getAchievement(Long id);

    Achievement getAchievementByTitle(String title);

    void createAchievement(Achievement achievement);
}

package faang.school.achievement.service;

import faang.school.achievement.model.dto.AchievementRedisDto;

public interface AchievementCache {

    public void addAchievementCache();

    public AchievementRedisDto getAchievementCache(String title);
}

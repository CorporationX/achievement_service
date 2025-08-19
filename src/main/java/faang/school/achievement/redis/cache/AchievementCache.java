package faang.school.achievement.redis.cache;

import faang.school.achievement.model.Achievement;

import java.util.Map;

public interface AchievementCache {
    Achievement get(String title);

    Map<String, Achievement> getAll();

    void flush();

    void put(Achievement achievement);

}

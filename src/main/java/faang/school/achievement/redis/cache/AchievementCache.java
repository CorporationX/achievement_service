package faang.school.achievement.redis.cache;

import faang.school.achievement.model.Achievement;

import java.util.Map;

public interface AchievementCache {
    public Achievement get(long id);

    public Map<Long, Achievement> getAll();

    public void flush();

    public void put(Achievement achievement);

}

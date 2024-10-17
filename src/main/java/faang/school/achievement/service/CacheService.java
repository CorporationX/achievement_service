package faang.school.achievement.service;

import java.time.Duration;
import java.util.List;

public interface CacheService<T> {

    void put(String key, T value);

    void put(String key, T value, Duration time);

    void put(String listKey, String key, T value);

    T get(String key, Class<T> clazz);

    T getFromMap(String mapName, String hashKey, Class<T> clazz);

    List<T> getValuesFromMap(String key, Class<T> clazz);

    boolean exists(String key);
}

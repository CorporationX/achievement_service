package faang.school.achievement.service.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.service.CacheService;
import io.lettuce.core.RedisConnectionException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisJsonCacheService<T> implements CacheService<T> {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void put(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void put(String key, T value, Duration time) {
        redisTemplate.opsForValue().set(key, value, time);
    }

    @Override
    public void put(String mapName, String hashKey, T value) {
        redisTemplate.opsForHash().put(mapName, hashKey, value);
    }

    @Override
    public T get(String key, Class<T> clazz) {
        Object jsonOrNull = redisTemplate.opsForValue().get(key);
        return get(jsonOrNull, clazz);
    }

    @Override
    public T getFromMap(String mapName, String hashKey, Class<T> clazz) {
        Object jsonOrNull = redisTemplate.opsForHash().get(mapName, hashKey);
        return get(jsonOrNull, clazz);
    }

    @Override
    public List<T> getValuesFromMap(String mapName, Class<T> clazz) {
        List<Object> jsonOrNull = redisTemplate.opsForHash().values(mapName);
        return jsonOrNull.stream()
                .map(json -> objectMapper.convertValue(json, clazz))
                .toList();
    }

    @Override
    public boolean exists(String key) {
        return Optional.ofNullable(redisTemplate.hasKey(key))
                .orElseThrow(() -> new RedisConnectionException("Redis connection failed"));
    }

    private T get(Object jsonOrNull, Class<T> clazz) {
        return Optional.ofNullable(jsonOrNull)
                .map(json -> objectMapper.convertValue(json, clazz))
                .orElse(null);
    }
}

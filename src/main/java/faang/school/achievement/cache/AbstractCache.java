package faang.school.achievement.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@RequiredArgsConstructor
public abstract class AbstractCache<T> {
    private static final String KEY_FORMAT = "%s:%s";

    private final RedisTemplate<String, T> redisCacheTemplate;
    private final String prefix;
    private final Duration duration;

    protected String key(String field) {
        return String.format(KEY_FORMAT, prefix, field);
    }

    protected void set(String key, T value) {
        redisCacheTemplate.opsForValue().set(key, value, duration);
    }

    protected T get(String key) {
        return redisCacheTemplate.opsForValue().get(key);
    }
}

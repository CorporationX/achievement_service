package faang.school.achievement.repository;

import faang.school.achievement.config.redis.properties.EventKeyCacheRedisProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventKeyCacheRepository {
    private final RedisTemplate<String, String> eventKeyCacheRedisTemplate;
    private final EventKeyCacheRedisProperties eventKeyCacheRedisProperties;

    public void save(String key) {
        eventKeyCacheRedisTemplate.opsForValue().set(
                formKey(key),
                "",
                eventKeyCacheRedisProperties.timeToLive()
        );
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(eventKeyCacheRedisTemplate.opsForValue().get(formKey(key)));
    }

    private String formKey(String hash) {
        return String.format("%s::%s", eventKeyCacheRedisProperties.name(), hash);
    }
}

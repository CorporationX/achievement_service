package faang.school.achievement.config.redis.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "event-key-cache.redis")
public record EventKeyCacheRedisProperties(
        String name,
        Duration timeToLive
) {
}

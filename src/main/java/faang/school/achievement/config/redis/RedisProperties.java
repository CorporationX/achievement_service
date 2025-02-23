package faang.school.achievement.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.redis")
public record RedisProperties(
        int port,
        String host,
        Channel channel) {
    public record Channel (
            String achievement,
            String follower,
            String cacheUpdates
    ) {}

}


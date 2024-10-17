package faang.school.achievement.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "spring.data.redis")
public record RedisProperties(Map<String, String> channels) {
}

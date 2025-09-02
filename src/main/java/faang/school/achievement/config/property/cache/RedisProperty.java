package faang.school.achievement.config.property.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.temporal.ChronoUnit;

@ConfigurationProperties(prefix = "spring.data.redis")
public record RedisProperty(
        @DefaultValue("1") int ttl,
        @DefaultValue("DAYS") ChronoUnit ttlUnit,
        @DefaultValue("60") int defaultTtl,
        @DefaultValue("MINUTES") ChronoUnit defaultTtlUnit,
        Cache cache
) {
    public record Cache(
            @DefaultValue("achievement") String achievement
    ) {}
}

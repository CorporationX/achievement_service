package faang.school.achievement.config.threads;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("thread.pool")
public record ThreadPoolProperties(
        int coreSize,
        int maxSize,
        String prefix
) {
}

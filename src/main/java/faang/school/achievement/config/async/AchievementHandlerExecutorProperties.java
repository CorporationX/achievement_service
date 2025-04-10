package faang.school.achievement.config.async;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "thread-pool.achievement-handler-executor")
public record AchievementHandlerExecutorProperties(
        int corePoolSize,
        int maxPoolSize,
        int queueCapacity,
        String namePrefix
) {
}

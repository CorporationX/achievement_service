package faang.school.achievement.config.async;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {

    @Value("${async.achievement.core-pool-size}")
    private int corePoolSize;

    @Value("${async.achievement.max-pool-size}")
    private int maxPoolSize;

    @Value("${async.achievement.queue-capacity}")
    private int queueCapacity;

    @Bean(name = "achievementHandlingExecutor")
    public ThreadPoolTaskExecutor achievementHandlingExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("AchievementAsync-");
        executor.initialize();
        return executor;
    }
}

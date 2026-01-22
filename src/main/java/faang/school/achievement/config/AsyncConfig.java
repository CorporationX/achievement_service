package faang.school.achievement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class AsyncConfig {

    @Value("${achievement.executor.core-pool-size}")
    private int achievementExecutorPoolSize;

    @Value("${achievement.executor.max-pool-size}")
    private int achievementExecutorMaxPoolSize;

    @Value("${achievement.executor.queue-capacity}")
    private int achievementExecutorQueueCapacity;

    @Value("${achievement.executor.alive-seconds}")
    private int achievementExecutorAliveSeconds;

    @Bean(name = "achievementExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(achievementExecutorPoolSize);
        executor.setMaxPoolSize(achievementExecutorMaxPoolSize);
        executor.setQueueCapacity(achievementExecutorQueueCapacity);
        executor.setKeepAliveSeconds(achievementExecutorAliveSeconds);

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize();
        return executor;
    }
}
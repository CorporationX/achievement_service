package faang.school.achievement.config.async;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@RequiredArgsConstructor
public class AsyncConfig {

    private final AchievementHandlerExecutorProperties achievementHandlerExecutorProperties;

    @Bean(name = "achievementHandlerExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(achievementHandlerExecutorProperties.corePoolSize());
        executor.setMaxPoolSize(achievementHandlerExecutorProperties.maxPoolSize());
        executor.setQueueCapacity(achievementHandlerExecutorProperties.queueCapacity());
        executor.setThreadNamePrefix(achievementHandlerExecutorProperties.namePrefix());
        executor.initialize();
        return executor;
    }
}

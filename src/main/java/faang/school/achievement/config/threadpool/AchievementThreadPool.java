package faang.school.achievement.config.threadpool;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@RequiredArgsConstructor
@EnableAsync
public class AchievementThreadPool {
    private final TaskExecutionPoolProperties properties;

    @Bean(name = "asyncExecutor")
    public ThreadPoolExecutor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(properties.getCoreSize());
        executor.setMaxPoolSize(properties.getMaxSize());
        executor.setQueueCapacity(properties.getQueueCapacity());
        executor.setKeepAliveSeconds(properties.getKeepAlive());
        executor.setThreadNamePrefix(properties.getThreadName());
        executor.initialize();
        return executor.getThreadPoolExecutor();
    }
}

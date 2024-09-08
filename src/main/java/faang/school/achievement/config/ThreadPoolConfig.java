package faang.school.achievement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ThreadPoolConfig {
    @Value("${spring.thread-pool.achievement-handler-pool-size}")
    private int achievementHandlerThreadPoolSize;

    @Bean
    public int achievementHandlerThreadPoolSize() {
        return achievementHandlerThreadPoolSize;
    }

    @Bean
    public ExecutorService achievementHandlerThreadPoolExecutor(
            int achievementHandlerThreadPoolSize
    ) {
        return Executors.newFixedThreadPool(achievementHandlerThreadPoolSize);
    }
}

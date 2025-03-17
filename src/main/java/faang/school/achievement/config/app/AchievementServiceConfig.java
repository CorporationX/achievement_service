package faang.school.achievement.config.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AchievementServiceConfig {

    private final int poolSize;

    public AchievementServiceConfig(@Value("${achievement-service.thread-pool-size}") int poolSize) {
        this.poolSize = poolSize;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    ExecutorService threadPool() {
        return Executors.newFixedThreadPool(poolSize);
    }
}

package faang.school.achievement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class CommonConfig {
    @Value("${thread-pool.size:7}")
    private int poolSize;

    @Bean
    public ExecutorService threadPool() {
        return Executors.newFixedThreadPool(poolSize);
    }
}

package faang.school.achievement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolConfig {

    @Value("${thread-pool.size}")
    private int threadPoolSize;

    @Bean
    public ExecutorService threadPool() {
        return Executors.newFixedThreadPool(threadPoolSize);
    }
}

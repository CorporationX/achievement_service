package faang.school.achievement.config.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AsyncConfig {

    @Bean
    public ExecutorService fixedExecutorService() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        int numberOfThreads = calculateNumberOfThreads(availableProcessors);
        return Executors.newFixedThreadPool(numberOfThreads);
    }

    private int calculateNumberOfThreads(int availableProcessors) {
        return availableProcessors * 2;
    }
}
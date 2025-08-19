package faang.school.achievement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfig {
    @Value("${spring.executor.multiplier}")
    private double coresMultiplier;

    @Bean
    public Executor postConstructExecutor() {
        int cores = Runtime.getRuntime().availableProcessors();
        int threadsCount = (int) Math.round(coresMultiplier * cores);
        return Executors.newFixedThreadPool(threadsCount);
    }
}

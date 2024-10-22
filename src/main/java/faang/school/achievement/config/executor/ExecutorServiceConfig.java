package faang.school.achievement.config.executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorServiceConfig {

    @Value("${achievement.executor.threads.count}")
    private int threadCount;

    @Bean
    public ExecutorService executor() {
        return Executors.newFixedThreadPool(threadCount);
    }
}

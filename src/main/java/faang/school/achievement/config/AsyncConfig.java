package faang.school.achievement.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@RequiredArgsConstructor
public class AsyncConfig implements AsyncConfigurer {

    @Bean
    public ExecutorService taskExecutor() {
        return Executors.newCachedThreadPool();
    }
}

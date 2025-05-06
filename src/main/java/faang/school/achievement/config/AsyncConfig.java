package faang.school.achievement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig{

    @Value("${async.pool.achievement.handsome}")
    private int handsomeAchievementPoolSize;

    @Bean
    public Executor handsomeAchievementPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(handsomeAchievementPoolSize);
        executor.setThreadNamePrefix("handsome-achievement-");
        executor.initialize();
        return executor;
    }
}

package faang.school.achievement.config;

import jakarta.validation.executable.ValidateOnExecution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class LikeAsyncHandleConfig {

    @Value("${achievement-async-config.like.pool-size}")
    private int poolSize;

    @Bean(name = "likeHandleAsync")
    public Executor createLikeHandleExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setThreadNamePrefix("likeHandleAsync-");
        return executor;
    }
}

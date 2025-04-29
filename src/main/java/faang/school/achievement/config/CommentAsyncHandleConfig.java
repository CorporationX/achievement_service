package faang.school.achievement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

@Component
public class CommentAsyncHandleConfig {

    @Value("${achievement-async-config.comment.pool-size}")
    private int poolSize;

    @Bean(name = "commentHandleAsync")
    public Executor createCommentHandleAsync() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setThreadNamePrefix("CommentHandleAsync-");
        executor.initialize();
        return executor;
    }
}

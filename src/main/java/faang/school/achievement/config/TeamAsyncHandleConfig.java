package faang.school.achievement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class TeamAsyncHandleConfig {

    @Value("${achievement-async-config.team.pool-size}")
    private int poolSize;

    @Bean(name = "teamHandleAsync")
    public Executor createTeamAsyncHandleExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setThreadNamePrefix("TeamHandleAsync-");
        return executor;
    }
}

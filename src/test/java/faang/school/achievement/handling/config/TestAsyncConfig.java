package faang.school.achievement.handling.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@TestConfiguration
public class TestAsyncConfig {

    @Bean(name = "achievementExecutor")
    @Primary
    public TaskExecutor achievementExecutor() {
        System.out.println("Using TestAsyncConfig...");
        return new SyncTaskExecutor();
    }
}

package faang.school.achievement.config.async;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.data.task.execution.pool")
public class ExecutorProperties {
    private int corePoolSize;
    private int maxPoolSize;
    private int queueCapacity;
}

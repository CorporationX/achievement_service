package faang.school.achievement.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@ConfigurationProperties(prefix = "async")
@Validated
public class AsyncProperties {

    private int corePoolSize;
    private int maxPoolSize;
    private int queueCapacity;
}

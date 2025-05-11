package faang.school.achievement.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@ConfigurationProperties(prefix = "async")
@Validated
public class AsyncProperties {

    @NotNull
    private Integer corePoolSize;

    @NotNull
    private Integer maxPoolSize;

    @NotNull
    private Integer queueCapacity;
}

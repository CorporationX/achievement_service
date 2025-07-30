package faang.school.achievement.config.kafka;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "spring.kafka")
@Getter
@Setter
@Validated
public class KafkaConsumerSettings {
    Map<String, ConsumerProperties> consumers = new HashMap<>();

    @Getter
    @Setter
    public static class ConsumerProperties {
        @NotBlank
        private String groupId;
        @Min(1)
        private int concurrency;
        @Min(1)
        private int maxPollRecords;
        @NotBlank
        private String offsetResetConfig;
    }
}

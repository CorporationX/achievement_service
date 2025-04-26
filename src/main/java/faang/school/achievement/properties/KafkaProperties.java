package faang.school.achievement.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.data.kafka")
public class KafkaProperties {
    private String bootstrapServers;
    private String groupId;
}

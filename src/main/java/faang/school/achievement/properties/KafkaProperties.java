package faang.school.achievement.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.kafka.producer")
public class KafkaProperties {
    private String bootstrapServers;
    private String acks;
    private Properties properties;
    private Topics topics;

    @Getter
    @Setter
    public static class Properties {
        private int deliveryMs;
        private int lignerMs;
        private int requestTimeoutMs;
    }

    @Getter
    @Setter
    public static class Topics {
        private String writerAchieved;
    }
}

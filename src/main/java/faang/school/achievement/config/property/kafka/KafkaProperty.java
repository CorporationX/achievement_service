package faang.school.achievement.config.property.kafka;

import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "spring.kafka")
public record KafkaProperty(
        @NonNull String bootstrapServers,
        Consumer consumer,
        @NestedConfigurationProperty
        BackOffProperty backoff,
        Topic topic
) {
    public record Consumer(
            @DefaultValue("events") String groupId,
            @DefaultValue("earliest") String autoOffsetReset
    ) {}

    public record Topic(
            @NonNull String commentNew
    ) {}
}

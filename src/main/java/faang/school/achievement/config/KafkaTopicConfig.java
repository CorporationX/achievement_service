package faang.school.achievement.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.consumer.topic-name}")
    private String postTopicName;

    @Bean
    public NewTopic postTopic() {
        return TopicBuilder
                .name(postTopicName)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
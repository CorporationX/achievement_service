package faang.school.achievement.infrastructure.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic commentCreateEventsTopic(
            @Value("${app.kafka.topics.comment-create-events:comment-create-events}") String topicName) {
        return TopicBuilder.name(topicName).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic commentDlqTopic(
            @Value("${app.kafka.topics.comment-create-events-dlq:comment-create-events-dlq}") String topicName) {
        return TopicBuilder.name(topicName).partitions(3).replicas(1).build();
    }
}
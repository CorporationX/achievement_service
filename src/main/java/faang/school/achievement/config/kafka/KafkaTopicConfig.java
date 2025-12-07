package faang.school.achievement.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic commentCreateEventsTopic(
            @Value("${app.kafka.topics.comment-create-topic:comment-create-topic}") String topicName) {
        return TopicBuilder.name(topicName).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic commentDlqTopic(
            @Value("${app.kafka.topics.comment-create-topic-dlq:comment-create-topic-dlq}") String topicName) {
        return TopicBuilder.name(topicName).partitions(3).replicas(1).build();
    }
}
package faang.school.achievement.config.kafka;

import faang.school.achievement.config.properties.HashtagAchievementTopicProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicsConfig {

    private final HashtagAchievementTopicProperties hashtagAchievementTopic;

    @Bean
    public NewTopic hashtagAchievementTopic() {
        return createTopic(hashtagAchievementTopic.getName(),
                hashtagAchievementTopic.getPartitions(),
                hashtagAchievementTopic.getReplicas());
    }

    private NewTopic createTopic(String name, int partitions, int replicas) {
        return TopicBuilder.name(name)
                .partitions(partitions)
                .replicas(replicas)
                .build();
    }
}

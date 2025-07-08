package faang.school.achievement.config.context;

import faang.school.achievement.dto.AchievementEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AchievementEventPublisher {
    private final KafkaTemplate<String, AchievementEvent> kafkaTemplate;

    @Value("${spring.kafka.topics.achievement:user_achievement}")
    private String topicName;

    public void publish(AchievementEvent event) {
        kafkaTemplate.send(topicName, event);
    }
}

package faang.school.achievement.publisher;

import faang.school.achievement.dto.AchievementEvent;
import faang.school.achievement.mapper.AchievementEventMapper;
import faang.school.achievement.protobuf.generate.AchievementEventProto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AchievementPublisher implements EventPublisher<AchievementEvent> {

    private final KafkaTemplate<byte[], byte[]> kafkaTemplate;
    private final AchievementEventMapper achievementEventMapper;

    @Value("${spring.kafka.topics.achievement.name}")
    private String topicName;

    @Override
    public void publish(AchievementEvent event) {
        AchievementEventProto.AchievementEvent protoEvent = achievementEventMapper.toProto(event);
        byte[] byteEvent = protoEvent.toByteArray();
        kafkaTemplate.send(topicName, byteEvent);
    }
}

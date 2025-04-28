package faang.school.achievement.sender;

import faang.school.achievement.dto.event.AchievementEventDto;
import faang.school.achievement.properties.KafkaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WriterAchievementEventSender implements AchievementEventSender {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    @Override
    public void send(AchievementEventDto achievementEvent) {
        kafkaTemplate.send(kafkaProperties.getTopics().getWriterAchieved(), achievementEvent);
        log.info("Send event {} to Kafka in topic {}", achievementEvent, kafkaProperties.getTopics().getWriterAchieved());
    }

    @Override
    public String getTitle() {
        return AchievementEventType.WRITER.getTitle();
    }
}

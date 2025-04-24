package faang.school.achievement.sender;

import faang.school.achievement.dto.event.AchievementEventDto;
import faang.school.achievement.properties.KafkaProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WriterAchievementEventSenderTest {

    @InjectMocks
    private WriterAchievementEventSender writerAchievementEventSender;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private KafkaProperties kafkaProperties;

    private final String title = "WRITER";
    private final AchievementEventDto achievementEvent = new AchievementEventDto();
    private final KafkaProperties.Topics topics = new KafkaProperties.Topics();

    @Test
    void send_ShouldSend() {
        topics.setWriterAchieved(title);

        when(kafkaProperties.getTopics()).thenReturn(topics);

        writerAchievementEventSender.send(achievementEvent);

        verify(kafkaTemplate, times(1)).send(topics.getWriterAchieved(), achievementEvent);
        verify(kafkaProperties, times(2)).getTopics();
    }

    @Test
    void getTitle_ShouldGet() {
        assertEquals(title, writerAchievementEventSender.getTitle());
    }
}
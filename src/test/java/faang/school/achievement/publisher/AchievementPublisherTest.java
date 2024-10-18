package faang.school.achievement.publisher;

import faang.school.achievement.dto.AchievementEvent;
import faang.school.achievement.mapper.AchievementEventMapperImpl;
import faang.school.achievement.protobuf.generate.AchievementEventProto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AchievementPublisherTest {

    @Mock
    private KafkaTemplate<byte[], byte[]> kafkaTemplate;

    @Spy
    private AchievementEventMapperImpl achievementEventMapper;

    @InjectMocks
    private AchievementPublisher achievementPublisher;

    private AchievementEvent achievementEvent;
    private AchievementEventProto.AchievementEvent protoEvent;
    private String topicName;

    @BeforeEach
    void setUp() {
        long userId = 1L, achievementId = 1L;
        achievementEvent = new AchievementEvent(LocalDateTime.now(), userId, achievementId);
        protoEvent = AchievementEventProto.AchievementEvent.newBuilder()
                .setUserId(userId)
                .setAchievementId(achievementId)
                .build();
        topicName = "achievement";
        ReflectionTestUtils.setField(achievementPublisher, "topicName", topicName);
    }

    @Test
    void publish_ShouldSendCorrectProtoEvent() {
        achievementPublisher.publish(achievementEvent);

        verify(kafkaTemplate).send(eq(topicName), any(byte[].class));
    }
}

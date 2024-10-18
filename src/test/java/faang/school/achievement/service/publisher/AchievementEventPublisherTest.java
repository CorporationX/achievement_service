package faang.school.achievement.service.publisher;

import faang.school.achievement.dto.event.AchievementEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementEventPublisherTest {

    @InjectMocks
    private AchievementEventPublisher achievementEventPublisher;

    @Mock
    private RedisTemplate redisTemplate;

    @Mock
    private ChannelTopic channelTopic;

    private static final String ACHIEVEMENT_EVENT_TOPIC = "AchievementEventTopic";

    @Test
    @DisplayName("Успешная отправка message")
    public void whenPublishEventShouldSuccess() {
        AchievementEvent event = AchievementEvent.builder().build();
        when(channelTopic.getTopic()).thenReturn(ACHIEVEMENT_EVENT_TOPIC);

        achievementEventPublisher.publish(event);

        verify(redisTemplate).convertAndSend(ACHIEVEMENT_EVENT_TOPIC, event);
    }
}
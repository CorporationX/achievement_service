package faang.school.achievement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.achievement.client.redis.AchievementRedisConfig;
import faang.school.achievement.client.redis.AchievementTopic;
import faang.school.achievement.dto.AchievementEvent;
import faang.school.achievement.publisher.AchievementPublisher;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {AchievementRedisConfig.class})
public class AchievementPublisherTest {

    @Captor
    private ArgumentCaptor<String> captorTopic;
    @Captor
    private ArgumentCaptor<String> captorMessage;
    @Autowired
    private AchievementTopic topic;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testPublishEvent() throws JsonProcessingException {
        // arrange
        AchievementEvent event = new AchievementEvent(
                1L,
                2L,
                3L,
                LocalDateTime.of(2023,12,1, 12, 0)
        );
        String topicString = "achievement_channel";
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String message = mapper.writeValueAsString(event);
        AchievementPublisher publisher = new AchievementPublisher(mapper, topic, redisTemplate);

        // act
        publisher.publish(event);
        verify(redisTemplate, times(1)).convertAndSend(captorTopic.capture(), captorMessage.capture());

        // assert
        Assertions.assertEquals(topicString, captorTopic.getValue());
        Assertions.assertEquals(message, captorMessage.getValue());
    }
 }

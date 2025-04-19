package faang.school.achievement.service.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.AchievementEvent;
import faang.school.achievement.exception.PublishAchievementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementPublisherTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ChannelTopic achievementChannel;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Executor asyncExecutor;

    @InjectMocks
    private AchievementPublisher achievementPublisher;

    private AchievementEvent event;

    @BeforeEach
    void setUp() {
        event = new AchievementEvent(
                1L, 1L, "COLLECTOR", "For 100 goals", 3, 15);

        doAnswer(invocation -> {
            Runnable task = invocation.getArgument(0);
            task.run();
            return null;
        }).when(asyncExecutor).execute(any(Runnable.class));
    }

    @Test
    void testPublishSuccessful() throws JsonProcessingException {
        String jsonMessage = "{\"userId\":1,\"achievementId\":1,\"achievementName\":\"COLLECTOR\"}";
        when(achievementChannel.getTopic()).thenReturn("achievement_channel");
        when(objectMapper.writeValueAsString(event)).thenReturn(jsonMessage);

        assertDoesNotThrow(() -> achievementPublisher.publish(event).join());
        verify(objectMapper).writeValueAsString(event);
        verify(redisTemplate).convertAndSend("achievement_channel", jsonMessage);
    }

    @Test
    void testPublishJsonProcessingExceptionThrowsPublishAchievementException() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(event)).thenThrow(new JsonProcessingException("Serialization failed") {
        });

        CompletionException completionException = assertThrows(CompletionException.class,
                () -> achievementPublisher.publish(event).join());
        Throwable cause = completionException.getCause();
        assertInstanceOf(PublishAchievementException.class, cause,
                "Expected PublishAchievementException, but was: " + cause.getClass().getSimpleName());
        assertEquals("Failed to serialize event to JSON",
                cause.getMessage(),
                "Exception message mismatch");

        verify(objectMapper).writeValueAsString(event);
        verify(redisTemplate, never()).convertAndSend(any(), any());
    }

    @Test
    void testPublishRedisExceptionThrowsPublishAchievementException() throws JsonProcessingException {
        String jsonMessage = "{\"userId\":1,\"achievementId\":1,\"achievementName\":\"COLLECTOR\"}";
        when(achievementChannel.getTopic()).thenReturn("achievement_channel");
        when(objectMapper.writeValueAsString(event)).thenReturn(jsonMessage);
        doThrow(new RuntimeException("Redis unavailable")).when(redisTemplate)
                .convertAndSend("achievement_channel", jsonMessage);

        CompletionException completionException = assertThrows(CompletionException.class,
                () -> achievementPublisher.publish(event).join());
        Throwable cause = completionException.getCause();
        assertInstanceOf(PublishAchievementException.class, cause,
                "Expected PublishAchievementException, but was: " + cause.getClass().getSimpleName());
        assertEquals("Failed to publish event to channel achievement_channel",
                cause.getMessage(), "Exception message mismatch");

        verify(objectMapper).writeValueAsString(event);
        verify(redisTemplate).convertAndSend("achievement_channel", jsonMessage);
    }
}
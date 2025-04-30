package faang.school.achievement.service.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.Event;
import faang.school.achievement.exception.PublishAchievementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExtendedAchievementPublisher {
    private final ChannelTopic achievementChannel;
    private final RedisTemplate<String, String> myRedisTemplate;
    private final ObjectMapper objectMapper;
    private final Executor asyncExecutor;

    public CompletableFuture<Void> publish(@NonNull Event event) {
        return CompletableFuture.runAsync(() -> {
            try {
                String message = objectMapper.writeValueAsString(event);
                log.info("Publishing event to channel '{}': {}",
                        achievementChannel.getTopic(), message);
                myRedisTemplate.convertAndSend(achievementChannel.getTopic(), message);
            } catch (JsonProcessingException e) {
                log.error("Failed to serialize event to JSON: {}", event, e);
                throw new PublishAchievementException("Failed to serialize event to JSON", e);
            } catch (Exception e) {
                log.error("Failed to publish event to channel '{}': {}",
                        achievementChannel.getTopic(), event, e);
                throw new PublishAchievementException("Failed to publish event to channel "
                        + achievementChannel.getTopic(), e);
            }
        }, asyncExecutor);
    }
}

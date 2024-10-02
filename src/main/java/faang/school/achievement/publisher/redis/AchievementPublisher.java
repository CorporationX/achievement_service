package faang.school.achievement.publisher.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.model.AchievementEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class AchievementPublisher implements MessagePublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;
    private final ObjectMapper objectMapper;

    public void publishMessage(AchievementEvent achievementEvent) {
        String message = null;
        try {
            message = objectMapper.writeValueAsString(achievementEvent);
        } catch (JsonProcessingException e) {
            log.warn("There was an exception during conversion AchievementEvent with ID = {} to String",
                    achievementEvent.getId());
        }

        redisTemplate.convertAndSend(topic.getTopic(), Objects.requireNonNull(message));
    }
}

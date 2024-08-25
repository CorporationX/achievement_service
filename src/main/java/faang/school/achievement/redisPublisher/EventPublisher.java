package faang.school.achievement.redisPublisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

@RequiredArgsConstructor
@Slf4j
public abstract class EventPublisher<T> {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;
    private final ObjectMapper objectMapper;

    public void publish(T event) {
        log.info("Publishing event: " + event);
        try {
            redisTemplate.convertAndSend(channelTopic.getTopic(), objectMapper.writeValueAsString(event));
        } catch (Exception e) {
            log.error("Error publishing event: " + event, e);
            throw new RuntimeException("Error publishing event: " + event, e);
        }
    }
}

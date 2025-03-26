package faang.school.achievement.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

public abstract class Publisher<T> {
    protected RedisTemplate<String, Object> redisTemplate;
    protected ObjectMapper objectMapper;

    protected void publish(T event) {
        try {
            String jsonEvent = objectMapper.writeValueAsString(event);
            redisTemplate.convertAndSend(topic().getTopic(), jsonEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка сериализации!", e);
        }
    }

    abstract ChannelTopic topic();
}

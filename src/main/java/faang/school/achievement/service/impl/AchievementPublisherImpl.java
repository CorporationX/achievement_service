package faang.school.achievement.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.config.redis.RedisProperties;
import faang.school.achievement.service.AchievementPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AchievementPublisherImpl implements AchievementPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final RedisProperties redisProperties;

    @Override
    public void publishMessage(Object message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            redisTemplate.convertAndSend(redisProperties.channel().achievement(), json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

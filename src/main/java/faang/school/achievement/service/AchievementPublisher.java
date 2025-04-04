package faang.school.achievement.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.AchievementEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class AchievementPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final ChannelTopic topic;

    @Autowired
    public AchievementPublisher(
            RedisTemplate<String, Object> redisTemplate,
            ObjectMapper objectMapper,
            @Qualifier("achievementTopic") ChannelTopic topic
    ) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.topic = topic;
    }

    public void publishAchievement(AchievementEvent event) {
        try {
            String jsonEvent = objectMapper.writeValueAsString(event);
            redisTemplate.convertAndSend(topic.getTopic(), jsonEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка сериализации!", e);
        }
    }
}

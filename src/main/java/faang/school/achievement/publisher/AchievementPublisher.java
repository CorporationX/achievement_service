package faang.school.achievement.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.AchievementEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.data.redis.channel.achievement}")
    private String topic;

    private final ObjectMapper mapper;

    public void publish(AchievementEvent achievementEvent) {
        try {
            String json = mapper.writeValueAsString(achievementEvent);
            redisTemplate.convertAndSend(topic, json);
        } catch (JsonProcessingException e) {
            log.error("Ошибка сериализации AchievementEvent: {}", achievementEvent, e);
        }
    }
}

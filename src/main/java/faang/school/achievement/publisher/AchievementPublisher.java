package faang.school.achievement.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.client.redis.AchievementTopic;
import faang.school.achievement.dto.AchievementEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class AchievementPublisher {
    private final ObjectMapper objectMapper;
    private final AchievementTopic topic;
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(AchievementEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            redisTemplate.convertAndSend(topic.getTopic(), message);
            log.info("Сообщение " + message + " отправлено в топик " + topic.getTopic());
        } catch (RuntimeException | JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Ошибка сервера");
        }
    }
}

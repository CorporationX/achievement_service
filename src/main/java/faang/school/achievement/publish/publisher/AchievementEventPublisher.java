package faang.school.achievement.publish.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.config.redis.RedisProperties;
import faang.school.achievement.dto.achievement.UserAchievementEventDto;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.UserAchievement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementEventPublisher {
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate redisTemplate;
    private final RedisProperties redisProperties;
    private final AchievementMapper achievementMapper;

    public void publishNewUserAchievementEventToBroker(UserAchievement newUserAchievement) {
        UserAchievementEventDto achievementEventDto = achievementMapper.toUserAchievementEventDto(newUserAchievement);
        String achievementEventChannelName = redisProperties.getAchievementEventChannelName();
        publish(achievementEventDto, achievementEventChannelName);
    }

    private void publish(Object message, String achievementEventChannelName) {
        try {
            String valueAsString = objectMapper.writeValueAsString(message);
            redisTemplate.convertAndSend(achievementEventChannelName, valueAsString);
            log.info("Send achievementEvent to Brokers channel: {} , message: {}", message, achievementEventChannelName);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}

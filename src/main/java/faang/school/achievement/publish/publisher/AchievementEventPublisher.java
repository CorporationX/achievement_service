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

    private void publish(Object message, String likeEventChannel) {
        String valueAsString;
        try {
            valueAsString = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        redisTemplate.convertAndSend(likeEventChannel, valueAsString);
        log.info("Send LikeEvent to Brokers channel: {} , message: {}", message, likeEventChannel);
    }
}

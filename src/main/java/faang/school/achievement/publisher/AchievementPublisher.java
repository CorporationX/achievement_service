package faang.school.achievement.publisher;

import faang.school.achievement.event.AchievementEvent;
import faang.school.achievement.exception.RedisPublishException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementPublisher {

    @Value("${spring.data.redis.channel.achievement}")
    private String achievementChannel;

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(AchievementEvent event) {
        try {
            redisTemplate.convertAndSend(achievementChannel, event);
            log.info("Published event to channel {}: {}", achievementChannel, event);
        } catch (DataAccessException ex) {
            throw new RedisPublishException(
                    String.format("Failed to publish event to channel %s due to Redis error: %s",
                            achievementChannel, ex.getMessage()), ex);
        } catch (IllegalArgumentException ex) {
            throw new RedisPublishException(
                    String.format("Invalid arguments for publishing event to channel %s: %s",
                            achievementChannel, ex.getMessage()), ex);
        } catch (Exception ex) {
            throw new RedisPublishException(
                    String.format("Unexpected error while publishing event to channel %s: %s",
                            achievementChannel, ex.getMessage()), ex
            );
        }
    }
}

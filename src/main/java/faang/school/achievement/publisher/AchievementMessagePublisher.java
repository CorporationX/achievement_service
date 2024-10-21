package faang.school.achievement.publisher;

import faang.school.achievement.dto.AchievementEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AchievementMessagePublisher implements MessagePublisher<AchievementEvent> {

    private final RedisTemplate<String, AchievementEvent> redisTemplate;
    private final ChannelTopic channelTopic;

    public AchievementMessagePublisher(RedisTemplate<String, AchievementEvent> redisTemplate,
                                       @Qualifier("achievementChannelTopic") ChannelTopic channelTopic) {
        this.redisTemplate = redisTemplate;
        this.channelTopic = channelTopic;
    }

    @Retryable(retryFor = {RuntimeException.class},
            backoff = @Backoff(delayExpression = "${spring.data.redis.publisher.delay}"))
    @Override
    public void publish(AchievementEvent event) {
        try {
            redisTemplate.convertAndSend(channelTopic.getTopic(), event);
            log.debug("Published achievement: {}", event);
        } catch (Exception e) {
            log.error("Failing to publish achievement {}", event, e);
            throw new RuntimeException(e);
        }
    }
}

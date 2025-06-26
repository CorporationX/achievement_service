package faang.school.achievement.publisher;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

public abstract class RedisEventPublisher<T> {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;

    public RedisEventPublisher(RedisTemplate<String, Object> redisTemplate, ChannelTopic channelTopic) {
        this.redisTemplate = redisTemplate;
        this.channelTopic = channelTopic;
    }

    public void publish(T event) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), event);
    }
}

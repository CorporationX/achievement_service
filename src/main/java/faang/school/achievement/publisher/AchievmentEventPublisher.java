package faang.school.achievement.publisher;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import faang.school.achievement.dto.AnalyticEventDto;

@Component
public class AchievmentEventPublisher extends RedisEventPublisher<AnalyticEventDto> {
    private RedisTemplate<String, Object> redisTemplate;
    private ChannelTopic channelTopic;
    
    public AchievmentEventPublisher(RedisTemplate<String, Object> redisTemplate, ChannelTopic channelTopic) {
        super(redisTemplate, channelTopic);
    }
}

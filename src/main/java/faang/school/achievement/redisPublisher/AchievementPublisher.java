package faang.school.achievement.redisPublisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.AchievementEventDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
public class AchievementPublisher extends EventPublisher<AchievementEventDto> {
    public AchievementPublisher(RedisTemplate<String, Object> redisTemplate,
                                @Qualifier("achievementTopic") ChannelTopic channelTopic,
                                ObjectMapper objectMapper) {
        super(redisTemplate, channelTopic, objectMapper);
    }
}

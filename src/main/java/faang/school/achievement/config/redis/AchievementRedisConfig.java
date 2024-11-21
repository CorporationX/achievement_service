package faang.school.achievement.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.publisher.AchievementPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

@Configuration
@RequiredArgsConstructor
public class AchievementRedisConfig {

    @Value("${spring.data.redis.channel.achievement}")
    private String achievementTopic;
    private final ObjectMapper objectMapper;

    @Bean
    public ChannelTopic achievementChannelTopic() {
        return new ChannelTopic(achievementTopic);
    }

    @Bean
    public AchievementPublisher achievementPublisher(
            RedisTemplate<String, Object> redisTemplate, ChannelTopic achievementChannelTopic) {
        return new AchievementPublisher(redisTemplate, achievementChannelTopic, objectMapper);
    }

}

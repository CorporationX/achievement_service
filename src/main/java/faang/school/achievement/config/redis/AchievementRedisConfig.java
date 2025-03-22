package faang.school.achievement.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;

@Configuration
public class AchievementRedisConfig extends RedisConfig {
    @Value("${spring.data.redis.channel.achievement}")
    private String channelTopic;

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic(channelTopic);
    }
}

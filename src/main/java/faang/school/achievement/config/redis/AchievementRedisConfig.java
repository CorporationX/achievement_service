package faang.school.achievement.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;

@Configuration
public class AchievementRedisConfig {

    @Value("${redis.topic}")
    private String topic;

    @Bean
    public ChannelTopic achievementTopic() {
        return new ChannelTopic(topic);
    }
}

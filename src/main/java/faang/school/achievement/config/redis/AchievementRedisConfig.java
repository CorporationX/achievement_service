package faang.school.achievement.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;

@Configuration
public class AchievementRedisConfig {

    @Value("${redis.publisher.topic}")
    private String achievementTopic;

    @Bean
    public ChannelTopic createAchievementTopic() {
        return new ChannelTopic(achievementTopic);
    }
}

package faang.school.achievement.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;

@Configuration
public class AchievementRedisConfig {

    @Bean
    public ChannelTopic achievementChannel() {
        return new ChannelTopic("achievement_channel");
    }
}
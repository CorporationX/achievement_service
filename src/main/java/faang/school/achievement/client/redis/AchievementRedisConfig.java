package faang.school.achievement.client.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AchievementRedisConfig {

    @Bean
    public AchievementTopic achievementTopic(@Value("${spring.data.redis.channel.achievement}") String name) {
        return new AchievementTopic(name);
    }
}

package faang.school.achievement.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
@RequiredArgsConstructor
public class AchievementRedisConfig {

    @Value("${spring.data.redis.channel.achievement}")
    private String channelTopic;

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic(channelTopic);
    }
}

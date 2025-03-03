package faang.school.achievement.config.redis;

import faang.school.achievement.listener.FollowMessageListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class FollowerEventConfig {

    @Bean
    ChannelTopic followTopic(
            @Value("${spring.data.redis.channel.follow}")
            String topic
    ) {
        return new ChannelTopic(topic);
    }

    @Bean
    MessageListenerAdapter followMessageListenerAdapter(
            FollowMessageListener followMessageListener
    ) {
        return new MessageListenerAdapter(followMessageListener);
    }
}

package faang.school.achievement.config;

import faang.school.achievement.listener.PostEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisPubSubConfig {
    @Value("${spring.data.redis.channels.post-channel}")
    private String postTopic;

    @Bean
    MessageListenerAdapter postEventListenerAdapter(PostEventListener eventListener) {
        return new MessageListenerAdapter(eventListener);
    }

    @Bean
    ChannelTopic postEventTopic() {
        return new ChannelTopic(postTopic);
    }

    @Bean
    RedisMessageListenerContainer postEventContainer(PostEventListener eventListener, RedisConnectionFactory factory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(eventListener, postEventTopic());
        return container;
    }
}

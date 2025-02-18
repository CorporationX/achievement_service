package faang.school.achievement.config.redis;

import faang.school.achievement.listener.TaskEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RedisListenerConfig {

    private final RedisProperties redisProperties;

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   MessageListenerAdapter listenerAdapter) {
        List<String> topics = redisProperties.getTopics();

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        topics.forEach(topic -> container
                .addMessageListener(listenerAdapter, new ChannelTopic(topic)));
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(TaskEventListener listener) {
        return new MessageListenerAdapter(listener, "onMessage");
    }
}

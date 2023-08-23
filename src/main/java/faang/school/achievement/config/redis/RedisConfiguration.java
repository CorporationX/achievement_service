package faang.school.achievement.config.redis;

import faang.school.achievement.message.RedisMessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;


@Configuration
public class RedisConfiguration {

    private final RedisMessagePublisher redisMessagePublisher;

    @Autowired
    public RedisConfiguration(RedisMessagePublisher redisMessagePublisher) {
        this.redisMessagePublisher = redisMessagePublisher;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(redisMessagePublisher);
        container.addMessageListener(messageListenerAdapter, new ChannelTopic("your-topic"));

        return container;
    }
}
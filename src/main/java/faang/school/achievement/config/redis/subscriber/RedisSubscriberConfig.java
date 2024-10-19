package faang.school.achievement.config.redis.subscriber;

import faang.school.achievement.listener.RedisMessageListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.List;

@Configuration
public class RedisSubscriberConfig {
    @Bean
    public RedisMessageListenerContainer redisContainer(JedisConnectionFactory jedisConnectionFactory,
                                                        List<RedisMessageListener> messageListeners) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);

        messageListeners.forEach(messageListener -> {
            container.addMessageListener(messageListener, messageListener.getTopic());
        });

        return container;
    }
}

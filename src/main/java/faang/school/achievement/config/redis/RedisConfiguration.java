package faang.school.achievement.config.redis;

import faang.school.achievement.listener.ProjectEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {
   private final RedisProperties redisProperties;

    @Bean
    MessageListenerAdapter projectEventListenerAdapter(ProjectEventListener projectEventListener) {
        return new MessageListenerAdapter(projectEventListener);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(ProjectEventListener projectEventListener, RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(projectEventListenerAdapter(projectEventListener), projectEventTopic());
        return container;
    }

    @Bean
    ChannelTopic projectEventTopic() {
        return new ChannelTopic(redisProperties.getChannels().get("project"));
    }
}

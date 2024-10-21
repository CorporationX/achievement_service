package faang.school.achievement.config.redis;

import faang.school.achievement.listener.LikeEventListener;
import faang.school.achievement.listener.ProjectEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    MessageListenerAdapter likeEventAdapter(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean(value = "likeChannelTopic")
    ChannelTopic likeChannelTopic(@Value("${spring.data.redis.channel.like-channel.name}") String likeChannelName) {
        return new ChannelTopic(likeChannelName);
    }

    @Bean(name = "projectChannel")
    public ChannelTopic projectChannel(@Value("${spring.data.redis.channel.project-channel.name}") String name) {
        return new ChannelTopic(name);
    }

    @Bean
    public MessageListenerAdapter projectListenerAdapter(
        @Qualifier("projectEventListener") ProjectEventListener projectEventListener
    ) {
        return new MessageListenerAdapter(projectEventListener);
    }


    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(
            LikeEventListener likeEventListener,
            @Qualifier("likeChannelTopic") ChannelTopic likeChannelTopic,
            @Qualifier("projectListenerAdapter") MessageListenerAdapter projectListenerAdapter,
            @Qualifier("projectChannel") ChannelTopic projectChannel

    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(likeEventListener, likeChannelTopic);
        container.addMessageListener(projectListenerAdapter, projectChannel);
        return container;
    }
}

package faang.school.achievement.config;

import faang.school.achievement.listener.CommentEventListener;
import faang.school.achievement.listener.PostEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final PostEventListener postEventListener;
    private final CommentEventListener commentEventListener;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.channel.post}")
    private String postEventChannelName;

    @Value("${spring.data.redis.channel.comment_event}")
    private String commentChannelName;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(postMessageListener(), postEventTopic());
        container.addMessageListener(commentMessageListener(), commentEventTopic());
        return container;
    }

    @Bean
    public MessageListenerAdapter postMessageListener() {
        return new MessageListenerAdapter(postEventListener);
    }

    @Bean
    public Topic postEventTopic() {
        return new ChannelTopic(postEventChannelName);
    }

    @Bean
    public MessageListener commentMessageListener() {
        return new MessageListenerAdapter(commentEventListener);
    }

    @Bean
    public Topic commentEventTopic() {
        return new ChannelTopic(commentChannelName);
    }
}

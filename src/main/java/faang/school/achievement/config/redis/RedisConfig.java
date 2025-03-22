package faang.school.achievement.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.listener.InviteSentEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.channels.invitation}")
    private String invitationTopicName;

    @Bean
    JedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    MessageListenerAdapter inviteSentEventMessageListener(InviteSentEventListener inviteSentEventListener) {
        return new MessageListenerAdapter(inviteSentEventListener);
    }

    @Bean
    ChannelTopic invitationTopic() {
        return new ChannelTopic(invitationTopicName);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(List<MessageListenerAdapter> listenerAdapters,
                                                 List<ChannelTopic> topics) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());

        IntStream.range(0, listenerAdapters.size()).forEach(i ->
                container.addMessageListener(listenerAdapters.get(i), topics.get(i))
        );
        return container;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}

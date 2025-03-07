package faang.school.achievement.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@Configuration
public class RedisConfig {

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(
            @Value("${spring.data.redis.host}") String host,
            @Value("${spring.data.redis.port}") int port
    ) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            JedisConnectionFactory jedisConnectionFactory,
            ObjectMapper objectMapper
    ) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        return template;
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(
            JedisConnectionFactory jedisConnectionFactory,
            MessageListenerAdapter followMessageListenerAdapter,
            MessageListenerAdapter mentorshipStartMessageListener,
            MessageListenerAdapter taskEventListenerAdapter,
            MessageListenerAdapter commentMessageListener,
            ChannelTopic followTopic,
            ChannelTopic mentorshipTopic,
            ChannelTopic taskTopic,
            ChannelTopic commentTopic
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);

        container.addMessageListener(followMessageListenerAdapter, followTopic);
        container.addMessageListener(mentorshipStartMessageListener, mentorshipTopic);
        container.addMessageListener(taskEventListenerAdapter, taskTopic);
        container.addMessageListener(commentMessageListener, commentTopic);
        return container;
    }
}

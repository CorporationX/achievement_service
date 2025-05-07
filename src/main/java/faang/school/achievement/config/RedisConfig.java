package faang.school.achievement.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.messaging.ProfilePicEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.Executors;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final MessageListener mentorshipEventListener;
    private final ObjectMapper objectMapper;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.channel.mentorship}")
    private String channelMentorship;

    @Value("${spring.data.redis.channel.profile-pic}")
    private String profilePicChannelName;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public MessageListenerAdapter mentorshipListener() {
        MessageListenerAdapter adapter = new MessageListenerAdapter(mentorshipEventListener);
        adapter.setSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        return adapter;
    }

    @Bean
    MessageListenerAdapter profilePicListener(ProfilePicEventListener profilePicEventListener) {
        return new MessageListenerAdapter(profilePicEventListener);
    }

    @Bean("profilePicChannel")
    public ChannelTopic profilePicChannel() {
        return new ChannelTopic(profilePicChannelName);
    }

    @Bean
    public ChannelTopic mentorshipTopic() {
        return new ChannelTopic(channelMentorship);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter profilePicListener) { //TODO подумать
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(mentorshipListener(), mentorshipTopic());
        container.addMessageListener(profilePicListener, profilePicChannel());

        container.setTaskExecutor(Executors.newFixedThreadPool(4));
        container.setSubscriptionExecutor(Executors.newFixedThreadPool(4));

        return container;
    }

    @Bean("redisCacheTemplate")
    public RedisTemplate<String, AchievementDto> redisCacheTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, AchievementDto> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(AchievementDto.class));
        return template;
    }
}
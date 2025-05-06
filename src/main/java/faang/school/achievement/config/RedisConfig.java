package faang.school.achievement.config;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.messaging.ProfilePicEventListener;
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
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.channel.profile-pic}")
    private String profilePicChannelName;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
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
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter profilePicListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(profilePicListener, profilePicChannel());
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

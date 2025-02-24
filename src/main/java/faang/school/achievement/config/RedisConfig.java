package faang.school.achievement.config;

import faang.school.achievement.messaging.MentorshipStartEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.channel.mentorship}")
    private String mentorshipTopic;

    @Value("${spring.data.redis.channel.achievement}")
    private String achievementTopic;

    private final MentorshipStartEventListener mentorshipStartEventListener;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    MessageListenerAdapter mentorshipStartListener(MentorshipStartEventListener mentorshipStartEventListener) {
        return new MessageListenerAdapter(mentorshipStartEventListener);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(messageListenerAdapter, mentorshipTopic());
        container.addMessageListener(messageListenerAdapter, createAchievementTopic());
        return container;
    }

    @Bean
    ChannelTopic mentorshipTopic() {
        return new ChannelTopic(mentorshipTopic);
    }

    @Bean
    public ChannelTopic createAchievementTopic() {
        return new ChannelTopic(achievementTopic);
    }
}

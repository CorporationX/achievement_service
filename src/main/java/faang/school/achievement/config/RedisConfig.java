package faang.school.achievement.config;

import faang.school.achievement.messaging.MentorshipStartEventListener;
import faang.school.achievement.messaging.TaskEventListener;
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

    @Value("${spring.data.redis.channel.task}")
    private String taskTopic;

    private final MentorshipStartEventListener mentorshipStartEventListener;
    private final TaskEventListener taskEventListener;

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
    MessageListenerAdapter mentorshipStartListener() {
        return new MessageListenerAdapter(mentorshipStartEventListener);
    }

    @Bean
    MessageListenerAdapter taskEventListenerAdapter() {
        return new MessageListenerAdapter(taskEventListener);
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());

        container.addMessageListener(mentorshipStartListener(), mentorshipTopic());
        container.addMessageListener(taskEventListenerAdapter(), taskTopic());

        return container;
    }

    @Bean
    ChannelTopic mentorshipTopic() {
        return new ChannelTopic(mentorshipTopic);
    }

    @Bean
    ChannelTopic taskTopic() {
        return new ChannelTopic(taskTopic);
    }

    @Bean
    public ChannelTopic achievementTopic() {
        return new ChannelTopic(achievementTopic);
    }
}

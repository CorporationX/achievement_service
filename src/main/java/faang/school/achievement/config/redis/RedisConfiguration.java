package faang.school.achievement.config.redis;

import faang.school.achievement.listener.GoalSetListener;
import faang.school.achievement.listener.MentorshipEventListener;
import faang.school.achievement.listener.TaskEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {
    @Value("${spring.data.redis.channels.mentorship_channel}")
    private String mentorshipEvent;
    private final MentorshipEventListener mentorshipEventListener;
    private final GoalSetListener goalSetListener;
    private final TaskEventListener taskEventListener;
    @Value("${spring.data.redis.channels.goal_set_channel.name}")
    private String goalSetChannel;
    @Value("${spring.data.redis.channels.task_channel.name}")
    private String taskChannel;
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(goalSetListener, new ChannelTopic(goalSetChannel));
        container.addMessageListener(mentorshipEventListener, new ChannelTopic(mentorshipEvent));
        container.addMessageListener(taskEventListener, new ChannelTopic(taskChannel));
        return container;
    }
}

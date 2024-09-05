package faang.school.achievement.config;

import faang.school.achievement.listener.achievement.AchievementListener;
import faang.school.achievement.listener.goal.GoalEventListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisMQConfig {
    @Value("${spring.data.redis.channel.achievement}")
    private String achievementTopicName;
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.connection_factory}")
    private String factoryType;
    @Value("${spring.data.redis.channel.goal}")
    private String goalTopicName;


    @Bean
    @Lazy
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration configuration =
                new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    @Lazy
    public LettuceConnectionFactory lettuceConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisConnectionFactory connectionFactory() {
        return switch (factoryType) {
            case "jedis" -> jedisConnectionFactory();
            case "lettuce" -> lettuceConnectionFactory();
            default -> throw new IllegalStateException("Unexpected value: " + factoryType);
        };
    }

    @Bean
    public ChannelTopic achievementTopic() {
        return new ChannelTopic(achievementTopicName);
    }

    @Bean
    public ChannelTopic goalTopic() {
        return new ChannelTopic(goalTopicName);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory
    ) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    MessageListenerAdapter achievementMessageListener(AchievementListener achievementListener) {
        return new MessageListenerAdapter(achievementListener);
    }

    @Bean
    MessageListenerAdapter goalMessageListener(GoalEventListener goalEventListener) {
        return new MessageListenerAdapter(goalEventListener);
    }

    @Bean
    RedisMessageListenerContainer achivmentRedisContainer(@Qualifier("achievementMessageListener") MessageListenerAdapter achievementListener) {
        return redisContainer(achievementListener, achievementTopic());
    }

    @Bean
    RedisMessageListenerContainer goalRedisContainer(@Qualifier("goalMessageListener") MessageListenerAdapter goalMessageListener) {
        return redisContainer(goalMessageListener, goalTopic());
    }


    RedisMessageListenerContainer redisContainer(MessageListenerAdapter listener, ChannelTopic topic) {
        RedisMessageListenerContainer container =
                new RedisMessageListenerContainer();

        container.setConnectionFactory(jedisConnectionFactory());

        container.addMessageListener(listener, topic);

        return container;
    }
}

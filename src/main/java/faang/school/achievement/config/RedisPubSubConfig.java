package faang.school.achievement.config;

import faang.school.achievement.dto.event.CommentEvent;
import faang.school.achievement.listener.CommentEventListener;
import faang.school.achievement.listener.TeamEventListener;
import faang.school.achievement.properties.RedisProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RedisPubSubConfig {

    private final RedisProperties redisProperties;

    @Bean
    public RedisTemplate<String, CommentEvent> redisPubSubTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, CommentEvent> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(CommentEvent.class));
        return template;
    }

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(redisProperties.getComment()));
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(CommentEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    MessageListenerAdapter teamListener(TeamEventListener teamEventListener) {
        return new MessageListenerAdapter(teamEventListener);
    }

    @Bean
    public CommandLineRunner verifyRedisConnection(RedisTemplate<String, CommentEvent> redisTemplate) {
        return args -> {
            try {
                String result = redisTemplate.getConnectionFactory().getConnection().ping();
                if (!"PONG".equals(result)) {
                    throw new IllegalArgumentException("Redis ping != PONG: " + result);
                }
                log.info("✅ Redis доступен: {}", result);
            } catch (Exception e) {
                log.error("❌ Ошибка подключения к Redis", e);
                throw new IllegalStateException("Не удалось подключиться к Redis", e);
            }
        };
    }
}

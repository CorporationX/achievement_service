package faang.school.achievement.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.listener.LikeEventListener;
import faang.school.achievement.listener.PostEventListener;
import faang.school.achievement.model.dto.AchievementRedisDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
@EnableCaching
public class RedisConfig {

    private final RedisProperties redisProperties;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }

    @Bean
    public RedisMessageListenerContainer container(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter postListener,
            MessageListenerAdapter likeListener
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(postListener, postTopic());
        container.addMessageListener(likeListener, likeTopic());

        return container;
    }

    @Bean
    public MessageListenerAdapter postListener(PostEventListener postEventListener) {
        return new MessageListenerAdapter(postEventListener);
    }

    @Bean
    public MessageListenerAdapter likeListener(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    public ChannelTopic likeTopic() {
        return new ChannelTopic(redisProperties.channels().get("like"));
    }

    @Bean
    public ChannelTopic postTopic() {
        return new ChannelTopic(redisProperties.channels().get("post"));
    }

    @Bean
    public ChannelTopic achievementTopic() {
        return new ChannelTopic(redisProperties.channels().get("achievement"));
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory,
                                     RedisCacheConfiguration redisCacheConfiguration) {
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(ObjectMapper objectMapper) {

        Jackson2JsonRedisSerializer<AchievementRedisDto> serializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, AchievementRedisDto.class);

        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(serializer));
    }
}

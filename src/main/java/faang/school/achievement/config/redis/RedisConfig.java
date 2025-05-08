package faang.school.achievement.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.config.redis.jedisconstants.JedisConstants;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.exception.RedisContainerIsEmptyException;
import faang.school.achievement.listener.RedisContainerMessageListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.ErrorHandler;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final Jackson2ObjectMapperBuilderCustomizer jsonCustomizer;
    private final Executor redisTaskExecutor;
    private final ErrorHandler redisErrorHandler;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    RedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(JedisConstants.JEDIS_POOL_CONFIG_MAX_TOTAL);
        poolConfig.setMaxIdle(JedisConstants.JEDIS_POOL_CONFIG_MAX_IDLE);
        poolConfig.setMinIdle(JedisConstants.JEDIS_POOL_CONFIG_MIN_IDLE);
        poolConfig.setMaxWait(JedisConstants.JEDIS_POOL_CONFIG_MAX_WAIT);

        JedisClientConfiguration clientConfiguration = JedisClientConfiguration.builder()
                .connectTimeout(JedisConstants.JEDIS_CLIENT_CONFIG_CONNECT_TIMEOUT)
                .readTimeout(JedisConstants.JEDIS_CLIENT_CONFIG_READ_TIMEOUT)
                .usePooling()
                .poolConfig(poolConfig)
                .build();

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(configuration, clientConfiguration);

        return jedisConnectionFactory;
    }

    @Bean
    RedisTemplate<String, Map<String, AchievementDto>> redisTemplate() {
        final RedisTemplate<String, Map<String, AchievementDto>> template = new RedisTemplate<>();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(createObjectMapper()));
        return template;
    }

    @Bean
    RedisTemplate<String, Object> redisTemplateForPublish() {
        final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(createObjectMapper(), Object.class));
        return redisTemplate;
    }

    @Bean
    RedisMessageListenerContainer redisContainer(List<RedisContainerMessageListener> listeners) {
        if (listeners.isEmpty()) {
            throw new RedisContainerIsEmptyException("No RedisContainerMessageListener beans found");
        }
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.setTaskExecutor(redisTaskExecutor);
        container.setErrorHandler(redisErrorHandler);
        listeners.forEach(listener -> container.addMessageListener(listener.getAdapter(), listener.getChannelTopic()));
        return container;
    }

    private ObjectMapper createObjectMapper() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        jsonCustomizer.customize(builder);
        return builder.build();
    }
}

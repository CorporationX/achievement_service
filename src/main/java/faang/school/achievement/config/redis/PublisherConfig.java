package faang.school.achievement.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class PublisherConfig {
    private static final int EXECUTOR_CORE_POOL_SIZE_MIN = 5;
    private static final int EXECUTOR_CORE_POOL_SIZE_MAX = 10;
    private static final int EXECUTOR_QUEUE_SIZE = 100;
    private static final String EXECUTOR_THREAD_NAME_PREFIX = "AsyncPublisher-";

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }

    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(EXECUTOR_CORE_POOL_SIZE_MIN);
        executor.setMaxPoolSize(EXECUTOR_CORE_POOL_SIZE_MAX);
        executor.setQueueCapacity(EXECUTOR_QUEUE_SIZE);
        executor.setThreadNamePrefix(EXECUTOR_THREAD_NAME_PREFIX);
        executor.initialize();
        return executor;
    }
}

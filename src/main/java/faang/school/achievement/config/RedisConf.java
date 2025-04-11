package faang.school.achievement.config;

import faang.school.achievement.dto.event.EventDto;
import faang.school.achievement.listener.EventListener;
import faang.school.achievement.propertie.RedisConnectionProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedisConf {
    private final RedisConnectionProperties redisConnectionProperties;
    private final EventListener eventListener;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig =
                new RedisStandaloneConfiguration(redisConnectionProperties.getHost(), redisConnectionProperties.getPort());

        JedisConnectionFactory factory = new JedisConnectionFactory(redisConfig);
        factory.afterPropertiesSet();
        log.info("Created JedisConnectionFactory with host {} and port {}, Redis connection status: {}",
                factory.getHostName(), factory.getPort(), factory.getConnection().isClosed() ? "DISCONNECTION" : "CONNECTION");
        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(EventDto.class));
        log.info("Initialized RedisTemplate with EventDto serializer");
        return template;
    }

    @Bean
    public MessageListenerAdapter eventListenerAdapter() {
        MessageListenerAdapter adapter =  new MessageListenerAdapter(eventListener);
        adapter.setSerializer(new Jackson2JsonRedisSerializer<>(EventDto.class));
        log.info("Created MessageListenerAdapter for listener {}", eventListener.getClass().getSimpleName());
        return adapter;
    }

    @Bean
    public List<ChannelTopic> eventTopics() {
        List<ChannelTopic> topics = redisConnectionProperties.getTopics().values().stream()
                .map(ChannelTopic::new)
                .toList();

        log.info("Subscribed to Redis topics: {}",
                topics.stream().map(ChannelTopic::getTopic).toList());

        return topics;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(eventListenerAdapter(), eventTopics());
        log.info("RedisMessageListenerContainer configured and subscribed to topics");
        return container;
    }
}
package faang.school.achievement.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.AchievementEvent;
import faang.school.achievement.listener.AchievementEventListener;
import faang.school.achievement.listener.LikeEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final ObjectMapper objectMapper;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, AchievementEvent> achievementEventRedisTemplate() {
        RedisTemplate<String, AchievementEvent> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, AchievementEvent.class));
        return template;
    }

    @Bean
    MessageListenerAdapter likeEventAdapter(LikeEventListener likeEventListener) {
        return new MessageListenerAdapter(likeEventListener);
    }

    @Bean
    public MessageListenerAdapter achievementEventAdapter(AchievementEventListener achievementEventListener) {
        return new MessageListenerAdapter(achievementEventListener);
    }

    @Bean(value = "likeChannelTopic")
    ChannelTopic likeChannelTopic(@Value("${spring.data.redis.channel.like-channel.name}") String likeChannelName) {
        return new ChannelTopic(likeChannelName);
    }

    @Bean(value = "achievementChannelTopic")
    public ChannelTopic achievementChannelTopic(
            @Value("${spring.data.redis.channel.achievement-channel.name}") String achievementChannelName) {
        return new ChannelTopic(achievementChannelName);
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(
            Map<String, MessageListenerAdapter> listenerAdapters,
            Map<String, ChannelTopic> channelTopics) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(listenerAdapters.get("likeEventAdapter"),
                channelTopics.get("likeChannelTopic"));
        container.addMessageListener(listenerAdapters.get("achievementEventAdapter"),
                channelTopics.get("achievementChannelTopic"));
        return container;
    }
}

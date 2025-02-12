package faang.school.achievement.config.redis;

import faang.school.achievement.handler.blogger.FollowerEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    private final String followChannel;

    public RedisConfig(@Value("${spring.data.redis.channel.follower}") String followChannel) {
        this.followChannel = followChannel;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, String> achievementRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public ChannelTopic followerTopic() {
        return new ChannelTopic(followChannel);
    }

    @Bean
    public MessageListenerAdapter messageAchievementListener(FollowerEventListener listener) {
        return new MessageListenerAdapter(listener, "onMessage");
    }

    @Bean
    public RedisMessageListenerContainer redisAchievementContainer(RedisConnectionFactory connectionFactory,
                                                                   MessageListenerAdapter listenerAdapter,
                                                                   ChannelTopic followerTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, followerTopic);
        return container;
    }
}

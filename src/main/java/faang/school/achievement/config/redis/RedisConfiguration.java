package faang.school.achievement.config.redis;

import faang.school.achievement.listener.MentorshipStartEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {

    private final RedisProperties redisProperties;

    @Bean
    public ChannelTopic mentorshipStartEventTopic() {
        return new ChannelTopic(redisProperties.getChannels().getMentorshipStartEvent());
    }

    @Bean
    MessageListenerAdapter mentorshipStartEvent(MentorshipStartEventListener mentorshipStartEventListener) {
        return new MessageListenerAdapter(mentorshipStartEventListener);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter mentorshipStartEvent) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(mentorshipStartEvent, mentorshipStartEventTopic());
        return container;
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }
}

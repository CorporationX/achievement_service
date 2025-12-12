package faang.school.achievement.config;

import faang.school.achievement.listener.MentorshipEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    @Bean
    public ChannelTopic mentorshipChannel(@Value("${spring.data.redis.channel.mentorship}") String channelName) {
        return new ChannelTopic(channelName);
    }

    @Bean
    public MessageListenerAdapter mentorshipListenerAdapter(MentorshipEventListener mentorshipEventListener) {
        return new MessageListenerAdapter(mentorshipEventListener);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter mentorshipListenerAdapter,
            ChannelTopic mentorshipChannel) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(mentorshipListenerAdapter, mentorshipChannel);

        return container;
    }
}
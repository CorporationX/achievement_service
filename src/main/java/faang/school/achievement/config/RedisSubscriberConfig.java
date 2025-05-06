package faang.school.achievement.config;

import faang.school.achievement.listener.MentorshipEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class RedisSubscriberConfig {
    private final LettuceConnectionFactory connectionFactory;
    private final MentorshipEventListener eventListener;

    @Value("${redis.channel-topic}")
    private String channelTopicName;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(ChannelTopic channelTopic) {
        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory);
        listenerContainer.addMessageListener(new MessageListenerAdapter(eventListener, "onMessage"), channelTopic);
        return listenerContainer;
    }

    @Bean
    public ChannelTopic mentorshipTopic() {
        return new ChannelTopic(channelTopicName);
    }
}

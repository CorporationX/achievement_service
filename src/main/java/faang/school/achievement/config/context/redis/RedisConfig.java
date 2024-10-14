package faang.school.achievement.config.context.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public class RedisConfig {

    @Value("${spring.data.redis.channel.comment-channel}")
    private String topicName;

    @Bean
    public MessageListenerAdapter messageListener(RedisMessageConsumerMentorshipRequests consumerMentorshipRequests) {
        return new MessageListenerAdapter(consumerMentorshipRequests);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
                                                                       RedisMessageConsumerMentorshipRequests consumerMentorshipRequests) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        MessageListenerAdapter messageListenerAdapter = messageListener(consumerMentorshipRequests);
        container.addMessageListener(messageListenerAdapter, topic());

        return container;
    }

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic(topicName);
    }
}

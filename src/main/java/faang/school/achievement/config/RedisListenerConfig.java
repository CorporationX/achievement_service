package faang.school.achievement.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.messaging.ProfilePicEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.util.concurrent.Executors;

@Configuration
@RequiredArgsConstructor
public class RedisListenerConfig {

    private final MessageListener mentorshipEventListener;
    private final ProfilePicEventListener profilePicEventListener;
    private final ObjectMapper objectMapper;
    private final JedisConnectionFactory jedisConnectionFactory;

    @Value("${spring.data.redis.channel.mentorship}")
    private String channelMentorship;

    @Value("${spring.data.redis.channel.profile-pic}")
    private String profilePicChannelName;

    @Bean
    public MessageListenerAdapter mentorshipListener() {
        MessageListenerAdapter adapter = new MessageListenerAdapter(mentorshipEventListener);
        adapter.setSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        return adapter;
    }

    @Bean
    MessageListenerAdapter profilePicListener() {
        return new MessageListenerAdapter(profilePicEventListener);
    }

    @Bean("profilePicChannel")
    public ChannelTopic profilePicChannel() {
        return new ChannelTopic(profilePicChannelName);
    }

    @Bean
    public ChannelTopic mentorshipTopic() {
        return new ChannelTopic(channelMentorship);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        container.addMessageListener(mentorshipListener(), mentorshipTopic());
        container.addMessageListener(profilePicListener(), profilePicChannel());

        container.setTaskExecutor(Executors.newFixedThreadPool(4));
        container.setSubscriptionExecutor(Executors.newFixedThreadPool(4));

        return container;
    }
}

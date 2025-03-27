package faang.school.achievement.config.redis;

import faang.school.achievement.listener.InviteSentEventListener;
import faang.school.achievement.listener.PostEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.List;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.channels.invitation}")
    private String invitationTopicName;

    @Value("${spring.data.redis.channels.post}")
    private String postChannel;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    MessageListenerAdapter inviteSentEventMessageListener(InviteSentEventListener inviteSentEventListener) {
        return new MessageListenerAdapter(inviteSentEventListener);
    }

    @Bean
    MessageListenerAdapter postMessageListener(PostEventListener postEventListener) {
        return new MessageListenerAdapter(postEventListener);
    }

    @Bean
    public ChannelTopic post_channel_topic() {
        return new ChannelTopic(postChannel);
    }

    @Bean
    public ChannelTopic invitationTopic() {
        return new ChannelTopic(invitationTopicName);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(List<MessageListenerAdapter> listenerAdapters,
                                                        List<ChannelTopic> topics) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());

        IntStream.range(0, listenerAdapters.size()).forEach(i ->
                container.addMessageListener(listenerAdapters.get(i), topics.get(i))
        );
        return container;
    }
}
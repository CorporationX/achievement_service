package faang.school.achievement.config.redis;

import faang.school.achievement.listner.PostEventListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.listener.InviteSentEventListener;
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
    private final ObjectMapper objectMapper;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.channels.invitation}")
    private String invitationTopicName;

    @Value("${spring.data.redis.channels.post}")
    private String postChannel;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(config);
    JedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    MessageListenerAdapter inviteSentEventMessageListener(InviteSentEventListener inviteSentEventListener) {
        return new MessageListenerAdapter(inviteSentEventListener);
    MessageListenerAdapter postMessageListener(PostEventListener postEventListener) {
        return new MessageListenerAdapter(postEventListener);
    }

    @Bean
    public ChannelTopic post_channel_topic() {
        return new ChannelTopic(postChannel);
    ChannelTopic invitationTopic() {
        return new ChannelTopic(invitationTopicName);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter listenerAdapter) {
    RedisMessageListenerContainer redisContainer(List<MessageListenerAdapter> listenerAdapters,
                                                 List<ChannelTopic> topics) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(listenerAdapter, post_channel_topic());
        container.setConnectionFactory(connectionFactory());

        IntStream.range(0, listenerAdapters.size()).forEach(i ->
                container.addMessageListener(listenerAdapters.get(i), topics.get(i))
        );
        return container;
    }
}

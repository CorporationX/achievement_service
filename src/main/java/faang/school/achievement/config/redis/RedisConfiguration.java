//package faang.school.achievement.config.redis;

//import faang.school.achievement.listener.CommentEventListener;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.listener.ChannelTopic;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
//import org.springframework.data.redis.serializer.StringRedisSerializer;

//@Configuration
//public class RedisConfiguration {

//    @Value("${spring.data.redis.host}")
//    private String host;
//    @Value("${spring.data.redis.port}")
//    private int port;
//    @Value("${spring.data.redis.channel.comment_achievement}")
//    private String commentChannel;
//
//    @Bean
//    public JedisConnectionFactory JedisConnectionFactory() {
//        System.out.println(port);
//        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
//        return new JedisConnectionFactory(config);
//    }
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
//        return redisTemplate;
//    }
//
//    @Bean
//    MessageListenerAdapter commentListener(CommentEventListener commentEventListener) {
//        return new MessageListenerAdapter(commentEventListener);
//    }
//
//    @Bean
//    ChannelTopic commentChannel() {
//        return new ChannelTopic(commentChannel);
//    }
//
//    @Bean
//    RedisMessageListenerContainer redisContainer(MessageListenerAdapter commentListener) {
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(JedisConnectionFactory());
//        container.addMessageListener(commentListener, commentChannel());
//        return container;
//    }
//}

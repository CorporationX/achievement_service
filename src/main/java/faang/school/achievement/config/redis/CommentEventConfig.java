package faang.school.achievement.config.redis;


import faang.school.achievement.listener.CommentEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class CommentEventConfig {
    @Value("${spring.data.redis.channel.comment}")
    String commentTopic;

    @Bean
    ChannelTopic commentTopic() {
        return new ChannelTopic(commentTopic);
    }

    @Bean
    MessageListenerAdapter commentMessageListener(CommentEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener);
    }
}

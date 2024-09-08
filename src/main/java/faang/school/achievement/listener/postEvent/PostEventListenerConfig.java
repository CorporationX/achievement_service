package faang.school.achievement.listener.postEvent;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;
@Configuration
@RequiredArgsConstructor
public class PostEventListenerConfig {
    @Value("${spring.data.redis.channel.post}")
    private String postChannelName;

    @Bean
    public MessageListenerAdapter postListenerAdapter(PostEventListener postEventListener) {
        return new MessageListenerAdapter(postEventListener);
    }

    @Bean
    public Pair<MessageListenerAdapter, ChannelTopic> postEvent(
            @Qualifier("postListenerAdapter") MessageListenerAdapter postListenerAdapter) {
        return Pair.of(postListenerAdapter, new ChannelTopic(postChannelName));
    }
}

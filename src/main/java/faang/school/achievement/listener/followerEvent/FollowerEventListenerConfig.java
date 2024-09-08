package faang.school.achievement.listener.followerEvent;

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
public class FollowerEventListenerConfig {
    @Value("${spring.data.redis.channel.follower}")
    private String followerChannelName;

    @Bean
    public MessageListenerAdapter followerEventListenerAdapter(FollowerEventListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    public Pair<MessageListenerAdapter, ChannelTopic> followerEvent(
            @Qualifier("followerEventListenerAdapter") MessageListenerAdapter followerEventListenerAdapter) {
        return Pair.of(followerEventListenerAdapter, new ChannelTopic(followerChannelName));
    }
}

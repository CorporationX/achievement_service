package faang.school.achievement.listener.teamEvent;

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
public class TeamEventListenerConfig {
    @Value("${spring.data.redis.channel.team}")
    private String channelForTeamEvent;

    @Bean
    public MessageListenerAdapter teamEventListenerAdapter(TeamEventListener teamEventListener) {
        return new MessageListenerAdapter(teamEventListener);
    }

    @Bean
    public Pair<MessageListenerAdapter, ChannelTopic> teamEvent(
            @Qualifier("teamEventListenerAdapter") MessageListenerAdapter teamEventListenerAdapter) {
        return Pair.of(teamEventListenerAdapter, new ChannelTopic(channelForTeamEvent));
    }
}

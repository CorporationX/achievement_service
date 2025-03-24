package faang.school.achievement.config.redis;

import faang.school.achievement.listener.InviteEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class InviteSentEventConfig {
    @Bean
    ChannelTopic inviteSentTopic(
    @Value("${spring.data.redis.channel.invitation_channel}")
    String inviteSentTopic){
        return new ChannelTopic(inviteSentTopic);
    }

    @Bean
    MessageListenerAdapter inviteSentStartMessageListener(InviteEventListener inviteEventListener) {
        return new MessageListenerAdapter(inviteEventListener);
    }

}

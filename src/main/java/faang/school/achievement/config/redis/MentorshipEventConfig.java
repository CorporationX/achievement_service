package faang.school.achievement.config.redis;

import faang.school.achievement.messaging.MentorshipStartEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class MentorshipEventConfig {
    @Value("${spring.data.redis.channel.mentorship}")
    private String mentorshipTopic;
    
    @Bean
    ChannelTopic mentorshipTopic() {
        return new ChannelTopic(mentorshipTopic);
    }

    @Bean
    MessageListenerAdapter mentorshipStartMessageListener(MentorshipStartEventListener mentorshipStartEventListener) {
        return new MessageListenerAdapter(mentorshipStartEventListener);
    }
}

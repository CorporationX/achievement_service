package faang.school.achievement.config.redis;

import faang.school.achievement.listener.ProjectMessageListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class ProjectEventConfig {
    @Bean
    ChannelTopic projectTopic(
            @Value("${spring.data.redis.channel.project_channel}")
            String projectTopic) {
        return new ChannelTopic(projectTopic);
    }

    @Bean
    MessageListenerAdapter projectStartMessageListener(ProjectMessageListener projectMessageListener) {
        return new MessageListenerAdapter(projectMessageListener);
    }
}

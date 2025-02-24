package faang.school.achievement.config.project;

import faang.school.achievement.listener.ProjectEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class ProjectRedisConfig {

    @Value("${spring.data.redis.channel.project_channel}")
    private String projectChannel;

    @Bean
    MessageListenerAdapter projectListenerAdapter(ProjectEventListener projectEventListener) {
        return new MessageListenerAdapter(projectEventListener);
    }

    @Bean
    Pair<MessageListenerAdapter, ChannelTopic> projectEvent(MessageListenerAdapter projectListenerAdapter) {
        return Pair.of(projectListenerAdapter, new ChannelTopic(projectChannel));
    }
}

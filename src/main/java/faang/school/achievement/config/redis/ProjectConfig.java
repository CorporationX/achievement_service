package faang.school.achievement.config.redis;

import faang.school.achievement.listener.ProjectListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
public class ProjectConfig {

    @Value("${spring.data.redis.channel.project_channel}")
    private String projectChannel;

    @Bean
    public ChannelTopic projectChannelTopic() {
        return new ChannelTopic(projectChannel);
    }

    @Bean
    MessageListenerAdapter projectEventListener(ProjectListenerAdapter projectListenerAdapter ){
        return new MessageListenerAdapter(projectListenerAdapter);
    }

    @Bean
    Pair<MessageListenerAdapter, ChannelTopic> projectRequester(MessageListenerAdapter projectEventListener) {
        return Pair.of(projectEventListener, projectChannelTopic());
    }
}

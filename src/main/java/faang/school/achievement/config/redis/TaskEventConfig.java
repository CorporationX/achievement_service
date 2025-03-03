package faang.school.achievement.config.redis;

import faang.school.achievement.listener.TaskEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class TaskEventConfig {
    @Bean
    MessageListenerAdapter taskEventListenerAdapter(TaskEventListener taskEventListener) {
        return new MessageListenerAdapter(taskEventListener);
    }

    @Bean
    ChannelTopic taskTopic(@Value("${spring.data.redis.channel.task}") String taskTopic) {
        return new ChannelTopic(taskTopic);
    }
}

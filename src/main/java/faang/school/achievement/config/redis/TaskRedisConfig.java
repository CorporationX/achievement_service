package faang.school.achievement.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;

@Configuration
@RequiredArgsConstructor
public class TaskRedisConfig {

    private final RedisProperties redisProperties;

    @Bean
    public ChannelTopic createTaskTopic() {
        String taskTopic = redisProperties.getTopics().get(0);
        return new ChannelTopic(taskTopic);
    }
}

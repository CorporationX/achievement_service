package faang.school.achievement.config.redis;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
public abstract class RedisMessageListener implements MessageListener {
    private final ChannelTopic topic;

    public RedisMessageListener(String topicName) {
        this.topic = new ChannelTopic(topicName);
    }

    public ChannelTopic getTopic() {
        return topic;
    }
}

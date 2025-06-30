package faang.school.achievement.config.redis;


import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;

public interface RedisMessageListener extends MessageListener {
    public ChannelTopic getTopic();
}

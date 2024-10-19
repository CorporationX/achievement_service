package faang.school.achievement.listener;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.Topic;

public interface RedisMessageListener extends MessageListener {
    Topic getTopic();
}

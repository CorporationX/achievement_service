package faang.school.achievement.listener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import faang.school.achievement.config.redis.RedisMessageListener;

@Component
public class PostCreateEventListener extends RedisMessageListener {
    public PostCreateEventListener(@Value("${spring.data.redis.channels.postCreated}")String topicName) {
        super(topicName);
    }
    
    @Override
    public void onMessage(Message message, @Nullable byte[] pattern) {
        throw new UnsupportedOperationException("Unimplemented method 'onMessage'");
    }

}

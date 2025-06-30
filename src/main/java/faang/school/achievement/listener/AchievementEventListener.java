package faang.school.achievement.listener;

import org.springframework.lang.Nullable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import faang.school.achievement.config.redis.RedisMessageListener;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AchievementEventListener implements RedisMessageListener {
    @Value("${spring.data.redis.channels.achievement}")
    private String topicName;

    @Override
    public void onMessage(@SuppressWarnings("null") Message message, @Nullable byte[] pattern) {
        throw new UnsupportedOperationException("Unimplemented method 'onMessage'for POST_CREATED channel");
    }

    @Override
    public ChannelTopic getTopic() {
        return new ChannelTopic(topicName);
    }
}

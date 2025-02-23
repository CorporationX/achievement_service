package faang.school.achievement.listener.follower;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.config.redis.RedisProperties;
import faang.school.achievement.event.FollowerEvent;
import faang.school.achievement.handler.AbstractAchievementHandler;
import faang.school.achievement.listener.AbstractEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FollowerEventListener extends AbstractEventListener {

    public FollowerEventListener(RedisMessageListenerContainer container,
                                 ObjectMapper objectMapper,
                                 List<AbstractAchievementHandler> handlers,
                                 RedisProperties redisProperties) {
        super(handlers, container, objectMapper, redisProperties);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, FollowerEvent.class, (event) -> {
            log.info("Catch event {}", event);
        });
    }

    @Override
    protected String getTopicName() {
        return getRedisProperties().channel().follower();
    }
}

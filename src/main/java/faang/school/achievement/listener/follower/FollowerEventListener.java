package faang.school.achievement.listener.follower;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.config.redis.RedisProperties;
import faang.school.achievement.event.FollowerEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.listener.AbstractEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> {

    public FollowerEventListener(List<EventHandler<FollowerEvent>> handlers,
                                 RedisMessageListenerContainer container,
                                 ObjectMapper objectMapper,
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

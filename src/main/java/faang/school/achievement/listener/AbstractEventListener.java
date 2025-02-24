package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.config.redis.RedisProperties;
import faang.school.achievement.handler.EventHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Component
public abstract class AbstractEventListener<T> implements MessageListener {
    @Autowired
    private final List<EventHandler<T>> handlers;
    //private final List<AbstractAchievementHandler> handlers;
    private final RedisMessageListenerContainer container;
    private final ObjectMapper objectMapper;
    private final RedisProperties redisProperties;

    @PostConstruct
    public void init() {
        container.addMessageListener(this, new ChannelTopic(getTopicName()));
        log.info("Listener {} registered on topic {}", this.getClass().getName(), getTopicName());
    }

    public void handleEvent(Message message, Class<T> eventType, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            for (EventHandler handler : handlers) {
                if (eventType.equals(handler.getInstance())) {
                    try {
                        handler.handleEvent(event);
                    } catch (Exception e) {
                        log.error("Error handling event {}. Error: {}", event, e);
                    }
                }
            }
            consumer.accept(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract String getTopicName();

    protected RedisProperties getRedisProperties() {
        return redisProperties;
    }

}

package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.config.redis.RedisProperties;
import faang.school.achievement.handler.EventHandler;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractEventListener<T> implements MessageListener {

    private final Map<Class<T>, EventHandler<T>> eventHandlersMap;
    private final RedisMessageListenerContainer container;
    private final ObjectMapper objectMapper;
    private final RedisProperties redisProperties;

    public AbstractEventListener(
            List<EventHandler<T>> eventHandlers,
            RedisMessageListenerContainer container,
            ObjectMapper objectMapper,
            RedisProperties redisProperties) {

        this.container = container;
        this.objectMapper = objectMapper;
        this.redisProperties = redisProperties;
        this.eventHandlersMap = eventHandlers.stream()
                .collect(Collectors.toMap(
                        EventHandler::getInstance,
                        handler -> handler,
                        (existingHandler, newHandler) -> {
                            log.warn("Duplicate handler found for event type: {}", existingHandler.getInstance());
                            return existingHandler;
                        }
                ));
    }

    @PostConstruct
    public void init() {
        container.addMessageListener(this, new ChannelTopic(getTopicName()));
        log.info("Listener {} registered on topic {}", this.getClass().getName(), getTopicName());
    }

    public void handleEvent(Message message, Class<T> eventType, Consumer<T> consumer) {
        try {
            log.info("Received message: {}", new String(message.getBody()));
            T event = objectMapper.readValue(message.getBody(), eventType);

            EventHandler<T> handler = eventHandlersMap.get(eventType);
            if (handler != null) {
                try {
                    handler.handleEvent(event);
                } catch (Exception e) {
                    log.error("Error handling event {}. Error: {}", event, e);
                }
            } else {
                log.warn("No handler found for event type: {}", eventType);
            }

            consumer.accept(event);
        } catch (IOException e) {
            log.error("Failed to deserialize message: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to deserialize message", e);
        }
    }

    protected abstract String getTopicName();

    protected RedisProperties getRedisProperties() {
        return redisProperties;
    }
}

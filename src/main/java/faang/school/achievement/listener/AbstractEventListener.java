package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.exception.ListenerProcessEventException;
import faang.school.achievement.exception.UnsupportedEventException;
import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T> implements MessageListener, RedisContainerMessageListener {

    private final ObjectMapper objectMapper;
    private final List<EventHandler<T>> handlers;

    public void processEvent(Message message, Class<T> eventType) {
        try {
            log.info("Received message for event: {}", eventType.getSimpleName());
            T event = objectMapper.readValue(message.getBody(), eventType);
            List<EventHandler<T>> supportedHandlers = handlers.stream()
                    .filter(handler -> handler.supportsEvent(eventType))
                    .toList();
            if (supportedHandlers.isEmpty()) {
                log.warn("No handlers found for event type: {}", eventType.getName());
                throw new UnsupportedEventException("No handlers support event: " + eventType.getName());
            }
            supportedHandlers.forEach(handler -> handler.handleEvent(event));
        } catch (IOException ex) {
            throw new ListenerProcessEventException(String.format("Unable to parse event: %s with message: %s",
                    eventType.getName(), message), ex);
        }
    }
}

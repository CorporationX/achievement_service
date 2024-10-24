package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@Component
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {

    protected final Map<Class<?>, List<EventHandler<?>>> handlers;
    protected final ObjectMapper objectMapper;

    protected void handleEvent(Message message, Class<T> type) {
        try {
            log.info("handleEvent() - start");
            T event = objectMapper.readValue(message.getBody(), type);
            log.debug("handleEvent() - event - {}", event);

            @SuppressWarnings("unchecked")
            List<EventHandler<T>> eventHandlers = (List<EventHandler<T>>) (List<?>) handlers.get(type);

            if (eventHandlers != null) {
                eventHandlers.forEach(handler -> handler.handle(event));
            } else {
                throw new NoSuchElementException("No handlers found for " + type + " type");
            }

            log.info("handleEvent() - finish, event - {} ", event);
        } catch (IOException | NoSuchElementException e) {
            log.error("Failed to handle event: {}", type, e);
            throw new RuntimeException(e);
        }
    }
}

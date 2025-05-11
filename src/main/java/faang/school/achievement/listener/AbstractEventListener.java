package faang.school.achievement.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.exception.JsonDeserializationException;
import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AbstractEventListener {

    private final ObjectMapper objectMapper;

    public <T> void processEvent(String message, Class<T> eventType, List<EventHandler<T>> handlers) {
        try {
            log.debug("Received new event: {}", message);
            T event = objectMapper.readValue(message, eventType);

            handlers.forEach(handler -> {
                handler.handleEvent(event);
                log.debug("Event processing on {} handler", handler.getClass().getSimpleName());
            });
        } catch (JsonProcessingException e) {
            throw new JsonDeserializationException("Deserialization json %s to event object error", message);
        }
    }
}

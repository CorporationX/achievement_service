package faang.school.achievement.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements RedisMessageListener {
    private final ObjectMapper objectMapper;
    private final Class<T> eventClass;
    private final List<EventHandler<T>> eventHandlers;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String jsonMessage = message.toString();
            T event = objectMapper.readValue(jsonMessage, eventClass);
            processEvent(event);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert json to event");
        } catch (Exception exception) {
            log.error("Failed to process event from Redis", exception);
        }
    }

    private void processEvent(T event) {
        eventHandlers.forEach(eventHandler -> eventHandler.handleEvent(event));
    }
}

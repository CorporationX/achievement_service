package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractMessageListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    protected final List<EventHandler<T>> handlers;

    protected void handleEvent(T event) {
        handlers.forEach(handler -> handler.handle(event));
    }

    protected T getEventFromBytes(byte[] body, Class<T> typeClass) {
        try {
            return objectMapper.readValue(body, typeClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

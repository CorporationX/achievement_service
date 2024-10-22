package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    private final ObjectMapper objectMapper;
    private final List<EventHandler<T>> handlers;

    public void handleEvent(Message message, Class<T> eventType){
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            handlers.forEach(handle -> handle.handle(event));
        } catch (IOException e) {
            log.error("Error during parsing message from listener: ", e);
            throw new RuntimeException(e);
        }
    }
}

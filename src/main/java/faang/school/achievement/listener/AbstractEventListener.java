package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.exception.JsonDeserializationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    private final ObjectMapper objectMapper;

    protected T deserializeJsonToEvent(Message message, Class<T> type) {
        T event;
        try {
            event = objectMapper.readValue(message.getBody(), type);
        } catch (IOException e) {
            throw new JsonDeserializationException(e);
        }
        return event;
    }
}

package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {

    private final ObjectMapper objectMapper;

    public T getEvent(Message message, Class<T> clazz) {
        try {
            return objectMapper.readValue(message.getBody(), clazz);
        } catch (IOException e) {
            throw new RuntimeException("Can't deserialize JSON");
        }
    }
}

package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.handler.AbstractEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {

    protected final List<AbstractEventHandler<T>> handlers;
    private final ObjectMapper objectMapper;

    public T handleEvent(Message message, Class<T> eventClass) {
        try {
            return objectMapper.readValue(message.getBody(), eventClass);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}

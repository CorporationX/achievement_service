package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.message.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    private final ObjectMapper objectMapper;
    private final List<EventHandler<T>> handlers;
    protected void onMessage(Message message, byte[] pattern) {
        try {
            T event = objectMapper.readValue(message.getBody(), getEventType());
            handlers.forEach(handler -> handler.handle(event));
        } catch (Exception e) {
            log.error(ErrorMessage.MESSAGE_PROCESSING_FAILED.format(new String(message.getBody())), e);
        }
    }

    protected abstract Class<T> getEventType();
}

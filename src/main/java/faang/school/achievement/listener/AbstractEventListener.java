package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.InviteSentEvent;
import faang.school.achievement.exception.EventHandlingException;
import faang.school.achievement.service.event_handler.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T> implements MessageListener {
    private final ObjectMapper objectMapper;
    protected final List<EventHandler<InviteSentEvent>> handlers;

    protected void handleEvent(Message message, Class<T> eventType, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            consumer.accept(event);
        } catch (IOException e) {
            String errorMessage = "Ошибка при обработке сообщения от слушателя";
            log.error("{} : {}", errorMessage, e.getMessage(), e);
            throw new EventHandlingException(errorMessage);
        }
    }
}

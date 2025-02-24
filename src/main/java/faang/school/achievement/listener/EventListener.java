package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.Event;
import faang.school.achievement.handler.ProjectEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class EventListener<T extends Event> implements MessageListener {
    private final ObjectMapper objectMapper;
    private final List<ProjectEventHandler<T>> eventHandlers;
    private final Class<T> tClass;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            T event = objectMapper.readValue(message.getBody(), tClass);
            eventHandlers.forEach(eventHandler -> eventHandler.handleEvent(event));
        } catch (IOException e) {
            log.error(String.format("Не удалось получить значение из JSON: %s", message));
            throw new RuntimeException(e);
        }
    }
}

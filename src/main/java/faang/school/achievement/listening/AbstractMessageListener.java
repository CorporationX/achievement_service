package faang.school.achievement.listening;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.handling.AbstractAchievementHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.util.List;

@Slf4j
public abstract class AbstractMessageListener<T, K extends AbstractAchievementHandler<T>> {
    protected final ObjectMapper objectMapper;
    protected final Class<T> eventClass;
    protected final List<K> eventHandlersByAchievement;

    protected AbstractMessageListener(
            ObjectMapper objectMapper,
            Class<T> eventClass,
            List<K> eventHandlersByAchievement
    ) {
        this.objectMapper = objectMapper;
        this.eventClass = eventClass;
        this.eventHandlersByAchievement = eventHandlersByAchievement;
    }

    protected void handleMessage(Message message) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventClass);
            eventHandlersByAchievement.forEach(handler -> handler.handle(event));
        } catch (Exception e) {
            log.error("Failed to handle message {}", message, e);
        }
    }
}
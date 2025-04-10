package faang.school.achievement.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.exception.EventDeserializationException;
import faang.school.achievement.handler.AbstractAchievementHandler;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractEventListener<E, H extends AbstractAchievementHandler<E>> {
    private final Class<E> eventType;
    private final List<H> eventHandlers;
    private final ObjectMapper objectMapper;

    public void consume(String message) {
        E event = deserializeEvent(message);
        eventHandlers.forEach(eventHandler -> eventHandler.handle(event));
    }

    private E deserializeEvent(String message) {
        try {
            return objectMapper.readValue(message, eventType);
        } catch (JsonProcessingException e) {
            throw new EventDeserializationException("An error occurred while deserializing the event");
        }
    }
}

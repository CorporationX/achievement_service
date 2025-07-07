package faang.school.achievement.kafka.listeners;

import faang.school.achievement.kafka.events.Event;
import faang.school.achievement.service.handlers.events.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class AbstractEventListener<T extends Event> implements EventListener<T> {
    private final Map<Class<? extends Event>, List<EventHandler>> handlersMap;

    public AbstractEventListener(List<EventHandler> handlers) {
        this.handlersMap = handlers.stream()
                .flatMap(handler -> handler.getEventTypes().stream()
                        .map(type -> Map.entry(type, handler)))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));
    }

    @Override
    public void handle(ConsumerRecord<String, T> message) {
        log.info("Handling message: {}", message);
        T value = message.value();
        List<EventHandler> handlers = handlersMap.get(value.getClass());

        if (handlers != null) {
            handlers.forEach(handler -> handler.handle(value));
        } else {
            log.warn("No handlers found for event type: {}", value.getClass().getSimpleName());
        }
    }
}

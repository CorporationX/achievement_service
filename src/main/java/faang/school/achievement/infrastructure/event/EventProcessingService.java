package faang.school.achievement.infrastructure.event;

import faang.school.achievement.exception.HandlersException;
import faang.school.achievement.handlers.EventHandler;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;

public interface EventProcessingService<T, H extends EventHandler<T>> {

    void process(String eventKey,
                 List<H> handlers,
                 T event,
                 Acknowledgment ack) throws HandlersException;
}
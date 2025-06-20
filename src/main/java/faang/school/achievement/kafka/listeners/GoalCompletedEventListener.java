package faang.school.achievement.kafka.listeners;

import faang.school.achievement.kafka.events.GoalCompletedEvent;
import faang.school.achievement.service.handlers.events.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class GoalCompletedEventListener extends AbstractEventListener<GoalCompletedEvent> {

    public GoalCompletedEventListener(List<EventHandler> handlers) {
        super(handlers);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.goal-completed.name}",
            containerFactory = "goalCompletedEventKafkaListenerContainerFactory"
    )
    public void listen(ConsumerRecord<String, GoalCompletedEvent> message, Acknowledgment ack) {
        log.info("Received goal completed event, message: {} ", message);
        handle(message);
        ack.acknowledge();
    }
}

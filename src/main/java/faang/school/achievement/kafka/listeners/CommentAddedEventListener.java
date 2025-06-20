package faang.school.achievement.kafka.listeners;

import faang.school.achievement.kafka.events.CommentAddedEvent;
import faang.school.achievement.service.handlers.events.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CommentAddedEventListener extends AbstractEventListener<CommentAddedEvent> {

    public CommentAddedEventListener(List<EventHandler> handlers) {
        super(handlers);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.comment-added.name}",
            containerFactory = "commentAddedEventKafkaListenerContainerFactory"
    )
    public void listen(ConsumerRecord<String, CommentAddedEvent> message, Acknowledgment ack) {
        log.info("Received comment added event, message: {} ", message);
        handle(message);
        ack.acknowledge();
    }
}

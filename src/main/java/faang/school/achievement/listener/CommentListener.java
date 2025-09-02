package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.exception.NonRetryableException;
import faang.school.achievement.exception.RetryableException;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.dto.event.CommentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
public class CommentListener extends AbstractEventListener<CommentEvent> {
    private final ObjectMapper objectMapper;

    public CommentListener(ObjectMapper objectMapper,
                           List<EventHandler<CommentEvent>> handlers) {
        super(handlers);
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.commentNew}",
            groupId = "achievement",
            containerFactory = "commentListenerContainerFactory")
    public void consume(String data) {
        try {
            CommentEvent commentEvent = objectMapper.readValue(data, CommentEvent.class);
            log.info("New event id={} received from Kafka: {}", commentEvent.id(), data);
            handleEvent(commentEvent);
        } catch (RetryableException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NonRetryableException(e);
        }
    }
}

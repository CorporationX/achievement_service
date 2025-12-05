package faang.school.achievement.listeners;

import faang.school.achievement.dto.CommentEventDto;
import faang.school.achievement.exception.HandlersException;
import faang.school.achievement.handlers.TimedEventHandler;
import faang.school.achievement.infrastructure.event.EventProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentEventListener {

    protected final EventProcessingService<CommentEventDto, TimedEventHandler<CommentEventDto>>
        commentEventProcessingService;
    protected final List<TimedEventHandler<CommentEventDto>> handlers;

    @RetryableTopic(attempts = "${app.kafka.retryable-topic.retry:3}",
                    backoff = @Backoff(delayExpression = "${app.kafka.retryable-topic.delay-ms:10000}"),
                    dltStrategy = DltStrategy.NO_DLT,
                    kafkaTemplate = "kafkaTemplate",
                    include = HandlersException.class)
    @KafkaListener(topics = "comment-create-events",
                   properties = "spring.json.value.default.type=faang.school.achievement.dto.CommentEventDto")
    public void onMessage(CommentEventDto event, Acknowledgment ack) {
        String eventKey = String.valueOf(event.commentId());
        commentEventProcessingService.process(eventKey, handlers, event, ack);
    }
}
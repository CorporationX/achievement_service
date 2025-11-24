package faang.school.achievement.listener;

import faang.school.achievement.dto.event.CommentEvent;
import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentEventListener {
    private final List<EventHandler<CommentEvent>> eventHandlers;

    @KafkaListener(
            topics = "${spring.kafka.topics.create-comment-topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void listen(CommentEvent commentEvent) {
        log.info("✅ Message received from Kafka: {}", commentEvent);
        List<CompletableFuture<Void>> completableFutures = new ArrayList<>();

        for (EventHandler<CommentEvent> eventHandler : eventHandlers) {
            completableFutures.add(eventHandler.handle(commentEvent));
        }

        for (CompletableFuture<Void> completableFuture : completableFutures) {
            completableFuture.exceptionally(ex -> {
                log.error("Error occurred while processing event", ex);
                throw new RuntimeException(ex);
            });
            completableFuture.join();
        }
    }
}
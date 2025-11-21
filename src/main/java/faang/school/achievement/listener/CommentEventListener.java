package faang.school.achievement.listener;

import faang.school.achievement.dto.event.CommentEvent;
import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentEventListener {
    private final List<EventHandler> eventHandlers;

    @KafkaListener(
            topics = "comment_topic",
            groupId = "achievement_group"
    )
    public void listen(CommentEvent event) {
        log.info("✅ Message received from Kafka: {}", event);
        eventHandlers.forEach(handler -> handler.handle(event));
    }
}


package faang.school.achievement.listener;

import faang.school.achievement.dto.event.CommentEvent;
import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentEventListener {
    private final List<EventHandler> eventHandlers;

    @KafkaListener(
            topics = "comment_topic",
            groupId = "achievement_group"
    )
    public void listen(CommentEvent event) {
        eventHandlers.forEach(handler -> handler.handle(event));
    }
}


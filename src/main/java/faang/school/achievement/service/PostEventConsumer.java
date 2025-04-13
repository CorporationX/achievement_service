package faang.school.achievement.service;

import faang.school.achievement.dto.PostEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostEventConsumer {
    private final List<EventHandler<PostEvent>> handlers;

    @KafkaListener(topics = "post-topic")
    public void consumePostEvent(PostEvent postEvent) {
        handlers.forEach(handler -> handler.handle(postEvent));
    }
}

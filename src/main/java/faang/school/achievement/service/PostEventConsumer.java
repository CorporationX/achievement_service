package faang.school.achievement.service;

import faang.school.achievement.dto.PostEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostEventConsumer {
    private final List<EventHandler<PostEvent>> handlers;

    @KafkaListener(
            topics = "${spring.kafka.consumer.topics.post.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumePostEvent(PostEvent postEvent) {
        handlers.forEach(handler -> handler.handle(postEvent));
    }
}

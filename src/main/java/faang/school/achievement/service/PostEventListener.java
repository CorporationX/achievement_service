package faang.school.achievement.service;

import faang.school.achievement.dto.PostEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import faang.school.achievement.utils.JsonUtils;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostEventListener {
    private final List<EventHandler<PostEvent>> handlers;
    private final JsonUtils jsonUtils;

    @KafkaListener(
            topics = "${spring.kafka.consumer.topics.post.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumePostEvent(String message) {
        try {
            PostEvent postEvent = jsonUtils.deserialize(message, PostEvent.class);
            handlers.forEach(handler -> handler.handle(postEvent));
        } catch (Exception e) {
            log.error("Ошибка десериализации PostEvent", e);
        }
    }
}

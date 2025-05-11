package faang.school.achievement.listener;

import faang.school.achievement.dto.HashtagRequestEvent;
import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HashtagRequestEventListener {

    private final AbstractEventListener eventListener;
    private final List<EventHandler<HashtagRequestEvent>> handlers;

    @KafkaListener(
            topics = "${spring.data.kafka.topics.hashtag-achievement.name}",
            groupId = "${spring.data.kafka.consumer.group-id}"
    )
    public void receive(String message) {
        eventListener.processEvent(message, HashtagRequestEvent.class, handlers);
    }
}

package faang.school.achievement.kafka.listeners;

import faang.school.achievement.kafka.events.ProfilePicEvent;
import faang.school.achievement.service.handlers.events.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ProfilePictureAddedEventListener extends AbstractEventListener<ProfilePicEvent>{
    public ProfilePictureAddedEventListener(List<EventHandler> handlers) {
        super(handlers);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.profile-picture-added.name}",
            containerFactory = "profilePictureAddedKafkaListenerContainerFactory"
    )
    public void listen(ConsumerRecord<String, ProfilePicEvent> message) {
        log.info("Received profile picture added event, message: {} ", message);
        handle(message);
    }
}

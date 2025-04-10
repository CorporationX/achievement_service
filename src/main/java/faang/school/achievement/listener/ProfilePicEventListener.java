package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.ProfilePicEvent;
import faang.school.achievement.handler.HandsomeAchievementHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfilePicEventListener extends AbstractEventListener<ProfilePicEvent, HandsomeAchievementHandler> {
    public ProfilePicEventListener(List<HandsomeAchievementHandler> eventHandlers, ObjectMapper objectMapper) {
        super(ProfilePicEvent.class, eventHandlers, objectMapper);
    }

    @KafkaListener(topics = "${spring.kafka.topic.profile-pic-topic.name:profile-pic-topic}",
            groupId = "${spring.kafka.consumer.group-id.profile-pic:profile-pic-group}")
    @Override
    public void consume(String message) {
        super.consume(message);
    }
}

package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.userprofile.ProfilePicEvent;
import faang.school.achievement.handler.profilepic.ProfilePicEventHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.serializer.support.SerializationFailedException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProfilePicEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final List<ProfilePicEventHandler> handlers;

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        log.info("Starting obtain ProfilePicEvent...");
        ProfilePicEvent event = toProfilePicEvent(message);

        log.info("Starting handling ProfilePicEvent...");
        for (ProfilePicEventHandler handler : handlers) {
            handler.handleEvent(event);
        };
    }

    private ProfilePicEvent toProfilePicEvent(Message message) {
        try {
            ProfilePicEvent event = objectMapper.readValue(message.getBody(), ProfilePicEvent.class);
            log.info("Received ProfilePicEvent for user {}", event.getUserId());
            return event;
        } catch (IOException exception) {
            log.error("Failed to deserialize ProfilePicEvent: {}", exception.getMessage());
            throw new SerializationFailedException("Event deserialization failed", exception);
        }
    }
}

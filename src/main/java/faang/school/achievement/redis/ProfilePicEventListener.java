package faang.school.achievement.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.mapper.EventMapper;
import faang.school.achievement.redis.event.ProfilePicRedisEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfilePicEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final EventMapper eventMapper;

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        log.info("Received message: {}", message);
        try {
            ProfilePicRedisEvent event = objectMapper.readValue(message.getBody(),
                    ProfilePicRedisEvent.class);
            eventPublisher.publishEvent(eventMapper.toProfilePicEvent(event, this));
        } catch (IOException e) {
            log.error("Error while processing message", e);
        }
    }
}

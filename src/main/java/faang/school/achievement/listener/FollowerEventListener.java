package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.FollowerEvent;
import faang.school.achievement.handler.follower.FollowerEventHandler;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.serializer.support.SerializationFailedException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FollowerEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final List<FollowerEventHandler> handlers;

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        try {
            FollowerEvent event = objectMapper.readValue(message.getBody(), FollowerEvent.class);
            log.info("Starting handling FollowerEvent: {}", event);

            for (FollowerEventHandler handler : handlers) {
                try {
                    handler.handleEvent(event);
                } catch (Exception e) {
                    log.error("Handler {} failed to process event", handler.getClass().getSimpleName(), e);
                }
            }

        } catch (IOException exception) {
            log.error("Failed to deserialize FollowerEvent: {}", exception.getMessage());
            throw new SerializationFailedException("Event deserialization failed", exception);
        }
    }
}
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
@Slf4j
@RequiredArgsConstructor
public class FollowerEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final List<FollowerEventHandler> handlers;

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        log.info("Received FollowerEvent message");

        FollowerEvent event = deserialize(message);

        handle(event);
    }

    private FollowerEvent deserialize(Message message) {
        try {
            return objectMapper.readValue(message.getBody(), FollowerEvent.class);
        } catch (IOException e) {
            log.error("Failed to deserialize FollowerEvent: {}", e.getMessage());
            throw new SerializationFailedException("Failed to deserialize FollowerEvent", e);
        }
    }

    private void handle(FollowerEvent event) {
        handlers.forEach(handler -> {
            try {
                handler.handleEvent(event);
            } catch (Exception e) {
                log.error("Handler {} failed", handler.getClass().getSimpleName(), e);
            }
        });
    }
}
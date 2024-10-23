package faang.school.achievement.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.ProjectEvent;
import faang.school.achievement.event.AchievementEventDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AchievementEventDispatcher<ProjectEvent> eventDispatcher;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received message from channel: {}", message.toString());
        try {
            ProjectEvent event = objectMapper.readValue(message.getBody(), ProjectEvent.class);
            eventDispatcher.dispatchEvent(event);
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Failed to map message to ProjectEvent: {}", jsonProcessingException.getMessage());
            throw new RuntimeException("Mapping error: " + jsonProcessingException.getMessage());
        } catch (IOException ioException) {
            log.error("I/O error occurred while processing message: {}", ioException.getMessage());
            throw new RuntimeException("I/O error while processing message: " + ioException.getMessage());
        }
    }
}

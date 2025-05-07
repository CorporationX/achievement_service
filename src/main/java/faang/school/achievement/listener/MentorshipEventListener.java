package faang.school.achievement.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.MentorshipStartEvent;
import faang.school.achievement.service.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MentorshipEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final List<EventHandler<MentorshipStartEvent>> handlers;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String json = new String(message.getBody());
        try {
            MentorshipStartEvent event = objectMapper.readValue(json, MentorshipStartEvent.class);
            log.info("Received MentorshipStartEvent: {}", event);
            handlers.forEach(hand -> hand.handle(event));
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize MentorshipStartEvent. Raw message: {}", json, e);
        } catch (RuntimeException e) {
            log.error("Unexpected error while handling MentorshipStartEvent. Raw message: {}", json, e);
        }
    }
}

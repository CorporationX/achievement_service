package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.handler.SenseyAchievementHandler;
import faang.school.achievement.model.MentorshipStartEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class MentorshipEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final List<EventHandler<MentorshipStartEvent>> handlers;

    @Override
    public void onMessage(Message message, byte[] pattern){
        try {
            MentorshipStartEvent event = objectMapper.readValue(message.getBody(), MentorshipStartEvent.class);

            log.info("Received mentorship event: {}", event);

            handlers.forEach(handler -> handler.handle(event));

        } catch (IOException e) {
            log.error("Failed to deserialize message", e);
        }
    }
}
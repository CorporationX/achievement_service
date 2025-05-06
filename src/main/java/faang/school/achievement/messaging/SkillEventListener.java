package faang.school.achievement.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.SkillAcquiredEvent;
import faang.school.achievement.messaging.handler.SkillEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SkillEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final List<SkillEventHandler> handlers;

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        try {
            SkillAcquiredEvent event = objectMapper.readValue(message.getBody(), SkillAcquiredEvent.class);
            log.info("✅ Received SkillAcquiredEvent: {}", event);
            handlers.forEach(handler -> handler.handleEvent(event));
        } catch (Exception e) {
            log.error("❌ Error getting event: {}", e.getMessage(), e);
        }
    }
}

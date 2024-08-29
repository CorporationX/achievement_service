package faang.school.achievement.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.MentorshipEvent;
import faang.school.achievement.handler.SenseyAchievementHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MentorshipEventListener implements MessageListener {
    private final SenseyAchievementHandler handler;
    private final ObjectMapper mapper;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            MentorshipEvent event = mapper.readValue(message.toString(), MentorshipEvent.class);
            handler.process(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

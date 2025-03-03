package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.messaging.MentorshipStartEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class MentorshipStartEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            objectMapper.readValue(message.getBody(), MentorshipStartEvent.class);
        } catch (IOException e) {
            log.error("Произошла ошибка при конвертации сообщения из Json в MentorshipStartEvent: {}", e.getMessage());
        }
    }
}

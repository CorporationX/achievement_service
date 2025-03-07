package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.messaging.CommentStartEvent;
import faang.school.achievement.handler.ExpertAchievementHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final ExpertAchievementHandler expertAchievementHandler;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            CommentStartEvent event = objectMapper.readValue(message.getBody(), CommentStartEvent.class);
            expertAchievementHandler.handle(event);
        } catch (IOException e) {
            log.error("Произошла ошибка при конвертации сообщения из Json в CommentStartEvent: {}", e.getMessage());
        }
    }
}

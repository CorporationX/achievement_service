package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.CommentEventDto;
import faang.school.achievement.handler.EvilCommenterAchievementHandler;
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
    private final EvilCommenterAchievementHandler evilCommenterAchievementHandler;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = new String(message.getBody());
        log.info("Get msg {}:", msg);
        try {
            CommentEventDto commentEventDto = objectMapper.readValue(msg, CommentEventDto.class);
            evilCommenterAchievementHandler.computeAchievement(commentEventDto);
        } catch (IOException exception) {
            log.error(exception.getMessage(), exception);
            throw new IllegalArgumentException(exception);
        }
    }
}

package faang.school.achievement.listener.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.comment.CommentEventDto;
import faang.school.achievement.handler.comment.CommentEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentEventListener implements MessageListener {

    private final List<CommentEventHandler> handlers;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            CommentEventDto commentEventDto = objectMapper.readValue(message.getBody(),
                    CommentEventDto.class);
            log.debug("Sending comment event to all handlers");
            handlers.forEach(handler -> handler.verifyAchievement(commentEventDto));
            log.debug("Sent event dto successfully");
        } catch (IOException e) {
            log.error("Something gone wrong while reading object {}", e.getMessage());
            throw new RuntimeException();
        }
    }
}

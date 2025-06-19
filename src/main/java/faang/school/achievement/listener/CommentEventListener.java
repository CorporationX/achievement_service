package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.event.CommentEventDto;
import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final List<EventHandler<CommentEventDto>> handlers;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            CommentEventDto event = objectMapper.readValue(message.getBody(), CommentEventDto.class);
            for (EventHandler<CommentEventDto> handler : handlers) {
                handler.handle(event);
            }
        } catch (Exception e) {
            log.error("Failed to process CommentEventDto", e);
        }
    }
}

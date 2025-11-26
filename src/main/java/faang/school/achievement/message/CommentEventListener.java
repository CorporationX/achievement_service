package faang.school.achievement.message;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.comment.CommentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.type.SerializationException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommentEventListener implements MessageListener {

    private final List<CommentEventHandler> eventHandlers;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        log.info("Received message: {}", message);
        CommentEvent commentEvent;
        try {
            commentEvent = objectMapper.readValue(message.getBody(), CommentEvent.class);
        } catch (IOException e) {
            log.error("Error while deserializing message", e);
            throw new SerializationException(e.getMessage(), e);
        }
        eventHandlers.forEach(handler -> handler.handle(commentEvent));
    }
}

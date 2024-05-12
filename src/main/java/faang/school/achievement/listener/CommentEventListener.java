package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.CommentEvent;
import faang.school.achievement.handler.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class CommentEventListener extends AbstractListener<CommentEvent> {
    public CommentEventListener(List<EventHandler<CommentEvent>> eventHandlers, ObjectMapper objectMapper) {
        super(objectMapper, eventHandlers);
    }

    @Override
    protected CommentEvent listenEvent(Message message) throws IOException {
        return objectMapper.readValue(message.getBody(), CommentEvent.class);
    }
}


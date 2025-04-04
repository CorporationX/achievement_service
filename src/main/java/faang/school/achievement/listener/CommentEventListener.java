package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.CommentEvent;
import faang.school.achievement.event_handler.EventHandler;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentEventListener extends AbstractEventListener<CommentEvent> {
    public CommentEventListener(ObjectMapper objectMapper,
                                List<EventHandler<CommentEvent>> handlers) {
        super(objectMapper, handlers);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        super.handleEvent(message, CommentEvent.class,
                event -> handlers.forEach(handler -> handler.handleEvent(event)));
    }
}

package faang.school.achievement.listner;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.PostEvent;
import faang.school.achievement.handler.EventHandler;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostEventListener extends AbstractEventListener<PostEvent> {

    public PostEventListener(ObjectMapper objectMapper, List<EventHandler<PostEvent>> handlers) {
        super(objectMapper, handlers);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        super.handleEvent(message, PostEvent.class,
                event -> handlers.forEach(handler -> handler.handle(event)));
    }
}
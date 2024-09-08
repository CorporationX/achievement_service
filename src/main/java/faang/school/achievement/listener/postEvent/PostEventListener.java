package faang.school.achievement.listener.postEvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.PostEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.listener.AbstractEventListener;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostEventListener extends AbstractEventListener<PostEvent> implements MessageListener {

    public PostEventListener(ObjectMapper objectMapper, List<EventHandler<PostEvent>> eventHandlers,
                             MessageSource messageSource) {
        super(objectMapper, eventHandlers, messageSource);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, PostEvent.class,
                postEvent -> eventHandlers.forEach(handler -> handler.process(postEvent)));
    }
}

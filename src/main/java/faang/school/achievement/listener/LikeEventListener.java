package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.model.event.LikeEvent;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LikeEventListener extends AbstractEventListener<LikeEvent> implements MessageListener {

    public LikeEventListener(ObjectMapper objectMapper, List<EventHandler<LikeEvent>> eventHandlers) {
        super(objectMapper, eventHandlers);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, LikeEvent.class);
    }
}

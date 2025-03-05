package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.follower.FollowEvent;
import faang.school.achievement.handler.EventHandler;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FollowMessageListener extends AbstractMessageListener<FollowEvent> {
    public FollowMessageListener(
            ObjectMapper objectMapper,
            List<EventHandler<FollowEvent>> eventHandlers
    ) {
        super(objectMapper, eventHandlers);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        FollowEvent event = getEventFromBytes(message.getBody(), FollowEvent.class);
        handleEvent(event);
    }
}

package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.InviteSentEvent;
import faang.school.achievement.handler.EventHandler;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InviteEventListener extends AbstractMessageListener<InviteSentEvent> {
    public InviteEventListener(ObjectMapper objectMapper,
                               List<EventHandler<InviteSentEvent>> eventHandlers) {
        super(objectMapper, eventHandlers);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        InviteSentEvent event = getEventFromBytes(message.getBody(), InviteSentEvent.class);
        handleEvent(event);
    }
}

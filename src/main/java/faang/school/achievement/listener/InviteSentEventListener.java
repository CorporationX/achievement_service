package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.InviteSentEvent;
import faang.school.achievement.event_handler.EventHandler;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InviteSentEventListener extends AbstractEventListener<InviteSentEvent> {
    public InviteSentEventListener(ObjectMapper objectMapper,
                                   List<EventHandler<InviteSentEvent>> handlers) {
        super(objectMapper, handlers);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        super.handleEvent(message, InviteSentEvent.class,
                event -> handlers.forEach(eventHandler -> eventHandler.handleEvent(event)));
    }
}

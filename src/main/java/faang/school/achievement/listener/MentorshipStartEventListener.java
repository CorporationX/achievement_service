package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.event.MentorshipStartEvent;
import faang.school.achievement.handler.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MentorshipStartEventListener extends AbstractEventListener<MentorshipStartEvent> {

    public MentorshipStartEventListener(ObjectMapper objectMapper,
                                        List<EventHandler<MentorshipStartEvent>> eventHandlers) {
        super(objectMapper, eventHandlers);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, MentorshipStartEvent.class, this::sendToHandler);
    }
}

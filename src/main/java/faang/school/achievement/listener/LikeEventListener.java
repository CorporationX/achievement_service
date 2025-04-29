package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.LikeEvent;
import faang.school.achievement.handler.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class LikeEventListener extends AbstractEventListener<LikeEvent> implements MessageListener {
    private static final String INFO_RECEIVED_LIKE_EVENT = "Received LikeEvent message: {}";

    public LikeEventListener(ObjectMapper objectMapper, List<EventHandler<LikeEvent>> eventHandlers) {
        super(objectMapper, eventHandlers);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info(INFO_RECEIVED_LIKE_EVENT, new String(message.getBody()));
        super.onMessage(message, pattern);
    }

    @Override
    protected Class<LikeEvent> getEventType() {
        return LikeEvent.class;
    }
}

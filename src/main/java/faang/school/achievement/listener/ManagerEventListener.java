package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.model.event.ManagerAchievementEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ManagerEventListener extends AbstractEventListener<ManagerAchievementEvent> implements MessageListener {
    public ManagerEventListener(ObjectMapper objectMapper, List<EventHandler<ManagerAchievementEvent>> eventHandlers) {
        super(objectMapper, eventHandlers);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("message is {}", message.toString());
        handleEvent(message, ManagerAchievementEvent.class);
    }
}

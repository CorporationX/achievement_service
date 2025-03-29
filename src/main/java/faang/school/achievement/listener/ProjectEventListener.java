package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.ProjectEvent;
import faang.school.achievement.event_handler.EventHandler;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectEventListener extends AbstractEventListener<ProjectEvent> {
    public ProjectEventListener(ObjectMapper objectMapper,
                                List<EventHandler<ProjectEvent>> list) {
        super(objectMapper, list);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        super.handleEvent(message, ProjectEvent.class,
                event -> handlers.forEach(eventHandler -> eventHandler.handleEvent(event)));
    }
}

package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.project.ProjectEvent;
import faang.school.achievement.handler.EventHandler;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectMessageListener extends AbstractMessageListener<ProjectEvent> {
    public ProjectMessageListener(ObjectMapper objectMapper,
                                  List<EventHandler<ProjectEvent>> projectEventHandlers
    ) {
        super(objectMapper, projectEventHandlers);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ProjectEvent event = getEventFromBytes(message.getBody(), ProjectEvent.class);
        handleEvent(event);
    }
}

package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.project.ProjectEvent;
import faang.school.achievement.handler.ProjectEventHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectEventListener extends EventListener<ProjectEvent> {

    public ProjectEventListener(ObjectMapper objectMapper,
                                List<ProjectEventHandler<ProjectEvent>> projectEventHandlers) {
        super(objectMapper, projectEventHandlers, ProjectEvent.class);
    }
}

package faang.school.achievement.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.ProjectEvent;
import faang.school.achievement.handler.project_event.BusinessmanAchievementHandler;
import faang.school.achievement.handler.project_event.ProjectEventAbstractHandler;
import faang.school.achievement.handler.project_event.ProjectEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component("projectEventListener")
@RequiredArgsConstructor
@Slf4j
public class ProjectEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final List<ProjectEventHandler> projectEventHandlers;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ProjectEvent projectEvent = getEvent(message.toString());
        projectEventHandlers.forEach(handler -> handler.handle(projectEvent));
    }

    private ProjectEvent getEvent(String message) {
        try {
            ProjectEvent projectEvent = objectMapper.readValue(message, ProjectEvent.class);
            log.info("Event is parsed: {}",projectEvent);
            return projectEvent;
        } catch (JsonProcessingException e) {
            log.error("Event parsing error: {}", message, e);
            throw new RuntimeException(e);
        }
    }
}

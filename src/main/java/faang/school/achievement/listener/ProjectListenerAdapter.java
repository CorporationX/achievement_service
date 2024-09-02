package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.ProjectEventDto;
import faang.school.achievement.handler.AchievementHandler;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectListenerAdapter extends RedisMessageListener<ProjectEventDto> implements MessageListener {

    private final List<AchievementHandler<ProjectEventDto>> achievementHandlers;

    public ProjectListenerAdapter(ObjectMapper objectMapper,
                                  List<AchievementHandler<ProjectEventDto>> achievementHandlers) {
        super(objectMapper);
        this.achievementHandlers = achievementHandlers;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ProjectEventDto projectEventDto = handleEvent(ProjectEventDto.class, message);
        achievementHandlers.forEach(handler -> handler.handle(projectEventDto));
    }
}

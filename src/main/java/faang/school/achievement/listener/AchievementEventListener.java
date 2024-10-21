package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.AchievementEvent;
import faang.school.achievement.handler.AbstractEventHandler;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AchievementEventListener extends AbstractEventListener<AchievementEvent> {

    public AchievementEventListener(List<AbstractEventHandler<AchievementEvent>> abstractEventHandlers,
                                    ObjectMapper objectMapper) {
        super(abstractEventHandlers, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        AchievementEvent achievementEvent = handleEvent(message, AchievementEvent.class);
        handlers.forEach(handler -> handler.handle(achievementEvent));
    }
}

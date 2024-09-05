package faang.school.achievement.listener.goal;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.goal.GoalSetEvent;
import faang.school.achievement.eventhandler.EventHandler;
import faang.school.achievement.listener.AbstractEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GoalEventListener extends AbstractEventListener<GoalSetEvent> {

    @Autowired
    public GoalEventListener(List<EventHandler<GoalSetEvent>> eventHandlers,
                             ObjectMapper mapper) {
        super(eventHandlers, mapper, GoalSetEvent.class);
    }
}

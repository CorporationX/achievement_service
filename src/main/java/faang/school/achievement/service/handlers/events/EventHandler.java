package faang.school.achievement.service.handlers.events;

import faang.school.achievement.kafka.events.Event;
import faang.school.achievement.model.AchievementCode;

import java.util.List;

public interface EventHandler {
    void handle(Event event);

    AchievementCode getCode();

    List<Class<? extends Event>> getEventTypes();
}
package faang.school.achievement.service.handlers.events;

import faang.school.achievement.kafka.events.CommentAddedEvent;
import faang.school.achievement.kafka.events.Event;
import faang.school.achievement.redis.RedisCounterService;
import faang.school.achievement.service.AchievementService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public abstract class CommentAddedEventHandler extends AbstractEventHandler {

    public CommentAddedEventHandler(AchievementService achievementService, RedisCounterService cacheService) {
        super(achievementService, cacheService);
    }

    @Override
    public List<Class<? extends Event>> getEventTypes() {
        return List.of(CommentAddedEvent.class);
    }
}
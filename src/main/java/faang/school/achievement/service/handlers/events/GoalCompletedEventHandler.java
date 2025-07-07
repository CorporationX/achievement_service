package faang.school.achievement.service.handlers.events;

import faang.school.achievement.kafka.events.Event;
import faang.school.achievement.kafka.events.GoalCompletedEvent;
import faang.school.achievement.redis.RedisCounterService;
import faang.school.achievement.service.AchievementService;

import java.util.List;

public abstract class GoalCompletedEventHandler extends AbstractEventHandler {
    public GoalCompletedEventHandler(AchievementService achievementService, RedisCounterService cacheService) {
        super(achievementService, cacheService);
    }

    @Override
    public List<Class<? extends Event>> getEventTypes() {
        return List.of(GoalCompletedEvent.class);
    }
}
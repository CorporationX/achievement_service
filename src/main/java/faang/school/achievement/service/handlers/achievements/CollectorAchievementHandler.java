package faang.school.achievement.service.handlers.achievements;

import faang.school.achievement.kafka.events.Event;
import faang.school.achievement.model.AchievementCode;
import faang.school.achievement.redis.RedisCounterService;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.handlers.events.GoalCompletedEventHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CollectorAchievementHandler extends GoalCompletedEventHandler {
    @Value("${achievements.collector.redis-counter-threshold}")
    private Long counterThreshold;

    public CollectorAchievementHandler(AchievementService achievementService, RedisCounterService cacheService) {
        super(achievementService, cacheService);
    }

    @Override
    public void handle(Event event) {
        super.handleSingleCounterCache(event, new AchievementCacheSettings(getCode(), counterThreshold));
    }

    @Override
    public AchievementCode getCode() {
        return AchievementCode.COLLECTOR;
    }
}
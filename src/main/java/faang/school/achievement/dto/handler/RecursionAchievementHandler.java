package faang.school.achievement.dto.handler;

import faang.school.achievement.dto.AchievementEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.CacheService;
import org.springframework.stereotype.Component;

@Component
public class RecursionAchievementHandler extends AchievementEventHandler<AchievementEvent> {

    public RecursionAchievementHandler(CacheService<String> cacheService,
                                       CacheService<Achievement> achievementCacheService,
                                       AchievementService achievementService) {
        super(cacheService, achievementCacheService, achievementService);
    }

    @Override
    protected String getAchievementName() {
        return "RECURSION-RECURSION";
    }

    @Override
    protected long getUserIdFromEvent(AchievementEvent event) {
        return event.getUserId();
    }

    @Override
    protected Class<AchievementEvent> getEventClass() {
        return AchievementEvent.class;
    }

    @Override
    protected Class<?> getHandlerClass() {
        return this.getClass();
    }
}

package faang.school.achievement.eventhandler.recommendation;

import faang.school.achievement.event.recommendation.RecommendationEvent;
import faang.school.achievement.eventhandler.AbstractAchievementHandler;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.achievement.AchievementService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NiceGuyAchievementHandler extends AbstractAchievementHandler<RecommendationEvent> {
    public NiceGuyAchievementHandler(
            AchievementService achievementService,
            AchievementCache achievementCache
    ) {
        super(achievementService, achievementCache, "NICE GUY");
    }

    @Override
    protected long getAchievingUserId(RecommendationEvent event) {
        return event.getRecommenderId();
    }

    @Async("achievementHandlerThreadPoolExecutor")
    @Override
    public void handle(RecommendationEvent event) {
        super.handle(event);
    }
}

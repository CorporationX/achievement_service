package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.service.AchievementService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NiceGuyAchievementHandler extends RecommendationEventHandler {

    private static final String ACHIEVEMENT_NAME = "NICE GUY";

    public NiceGuyAchievementHandler(AchievementCache achievementCache, AchievementService achievementService) {
        super(ACHIEVEMENT_NAME, achievementCache, achievementService);
    }
}

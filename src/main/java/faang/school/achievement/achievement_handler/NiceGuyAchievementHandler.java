package faang.school.achievement.achievement_handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.service.AchievementService;
import org.springframework.stereotype.Component;

@Component
public class NiceGuyAchievementHandler extends RecommendationEventHandler{
    private static final String NICE_GUY = "Nice guy";
    private static final int REQUIRED_PROGRESS = 10;

    public NiceGuyAchievementHandler(AchievementCache cache, AchievementService service) {
        super(cache, service, NICE_GUY, REQUIRED_PROGRESS);
    }
}

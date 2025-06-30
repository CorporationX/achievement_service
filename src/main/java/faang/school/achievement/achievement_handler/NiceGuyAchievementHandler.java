package faang.school.achievement.achievement_handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.event.RecommendationEvent;
import faang.school.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NiceGuyAchievementHandler extends AbstractEventHandler<RecommendationEvent> {
    public NiceGuyAchievementHandler(AchievementCache cache,
                                     AchievementService service,
                                     @Value("${achievement.niceguy.title}") String title,
                                     @Value("${achievement.niceguy.requiredProgress}") int requiredProgress) {
        super(cache, service, title, requiredProgress);
    }
}

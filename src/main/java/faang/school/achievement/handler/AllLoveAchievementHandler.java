package faang.school.achievement.handler;

import faang.school.achievement.model.event.LikeEvent;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AllLoveAchievementHandler extends AbstractEventHandler<LikeEvent> {
    public AllLoveAchievementHandler(
            AchievementCache achievementCache,
            AchievementService achievementService,
            @Value("${achievement-titles.all-love}")
            String achievementTitle
    ) {
        super(achievementCache, achievementService, achievementTitle);
    }
}

package faang.school.achievement.event_handler.project_create_event;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BusinessmanAchievementHandler extends ProjectCreateEventHandler{
    public BusinessmanAchievementHandler(AchievementCache achievementCache,
                                         AchievementService achievementService,
                                         @Value("${achievements.titles.businessman}")
                                         String achievementTitle) {
        super(achievementCache, achievementService, achievementTitle);
    }
}

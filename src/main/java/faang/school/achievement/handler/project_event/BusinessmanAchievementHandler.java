package faang.school.achievement.handler.project_event;


import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BusinessmanAchievementHandler extends ProjectEventAbstractHandler{

    public BusinessmanAchievementHandler(AchievementService achievementService,
                                         AchievementCache cache,
                                         @Value("${achievements.project-achievements.businessman.name}")
                                         String achievementTitle) {
        super(achievementService, cache, achievementTitle);
    }
}

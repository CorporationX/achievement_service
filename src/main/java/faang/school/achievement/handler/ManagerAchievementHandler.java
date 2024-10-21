package faang.school.achievement.handler;

import faang.school.achievement.model.event.ManagerAchievementEvent;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ManagerAchievementHandler extends AbstractEventHandler<ManagerAchievementEvent> {
    public ManagerAchievementHandler(AchievementCache achievementCache, AchievementService achievementService, @Value("${achievement-titles.manager}") String managerTitle) {
        super(achievementCache, achievementService, managerTitle);
    }
}

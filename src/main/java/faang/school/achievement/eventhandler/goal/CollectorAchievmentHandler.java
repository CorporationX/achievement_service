package faang.school.achievement.eventhandler.goal;

import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.achievement.AchievementService;
import org.springframework.stereotype.Component;

@Component
public class CollectorAchievmentHandler extends GoalEventHandler{
    private static final String ACHIEVEMENT_NAME = "COLLECTOR";
    public CollectorAchievmentHandler(AchievementService achievementService, AchievementCache achievementCache) {
        super(achievementService, achievementCache, ACHIEVEMENT_NAME);
    }
}

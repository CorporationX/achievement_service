package faang.school.achievement.handler.like;

import faang.school.achievement.service.AchievementService;
import org.springframework.stereotype.Component;

@Component
public class AllLoveAchievementHandler extends LikeEventHandler {

    public AllLoveAchievementHandler(AchievementService achievementService) {
        super(achievementService);
    }

    @Override
    protected String getAchievementName() {
        return "ALL_LOVE";
    }
}

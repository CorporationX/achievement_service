package faang.school.achievement.handler.mentorship;

import faang.school.achievement.service.achievement_progress.AchievementProgressServiceImpl;
import faang.school.achievement.service.cache.AchievementCacheServiceImpl;
import faang.school.achievement.service.user_achievement.UserAchievementServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class SenseiAchievementHandler extends MentorshipEventHandler {

    private final static String SENSEI = "sensei";

    public SenseiAchievementHandler(
            AchievementProgressServiceImpl achievementProgressService,
            AchievementCacheServiceImpl achievementCacheService,
            UserAchievementServiceImpl userAchievementService) {
        super(achievementProgressService, achievementCacheService, userAchievementService);
    }

    @Override
    protected String getAchievementTitle() {
        return SENSEI;
    }
}
package faang.school.achievement.handler.mentorship;

import faang.school.achievement.service.AchievementProgressService;
import faang.school.achievement.service.UserAchievementService;
import faang.school.achievement.service.cache.AchievementCacheService;
import org.springframework.stereotype.Component;

@Component
public class SenseiAchievementHandler extends MentorshipEventHandler {

    private final static String SENSEI = "sensei";

    public SenseiAchievementHandler(
            AchievementProgressService achievementProgressService,
            AchievementCacheService achievementCacheService,
            UserAchievementService userAchievementService) {
        super(achievementProgressService, achievementCacheService, userAchievementService);
    }

    @Override
    protected String getAchievementTitle() {
        return SENSEI;
    }
}
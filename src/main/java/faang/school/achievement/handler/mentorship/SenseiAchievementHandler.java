package faang.school.achievement.handler.mentorship;

import faang.school.achievement.service.achievementprogress.DefaultAchievementProgressServiceService;
import faang.school.achievement.service.cache.DefaultAchievementCacheService;
import faang.school.achievement.service.userachievement.DefaultUserAchievementService;
import org.springframework.stereotype.Component;

@Component
public class SenseiAchievementHandler extends MentorshipEventHandler {

    private final static String SENSEI = "sensei";

    public SenseiAchievementHandler(
            DefaultAchievementProgressServiceService achievementProgressService,
            DefaultAchievementCacheService achievementCacheService,
            DefaultUserAchievementService userAchievementService) {
        super(achievementProgressService, achievementCacheService, userAchievementService);
    }

    @Override
    protected String getAchievementTitle() {
        return SENSEI;
    }
}
package faang.school.achievement.handler;

import faang.school.achievement.event.ProjectEventDto;
import faang.school.achievement.service.AchievementProgressService;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.UserAchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component

public class BusinessmanAchievementHandler extends AchievementHandler<ProjectEventDto> {

    @Value("${achievements.businessman_achievement}")
    private String achievementName;

    public BusinessmanAchievementHandler(AchievementService achievementService, AchievementProgressService achievementProgressService, UserAchievementService userAchievementService) {
        super(achievementService, achievementProgressService, userAchievementService);
    }

    protected String getAchievementName() {
        return achievementName;
    }
}

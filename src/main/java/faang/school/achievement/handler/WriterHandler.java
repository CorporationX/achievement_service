package faang.school.achievement.handler;

import faang.school.achievement.dto.PostEventDto;
import faang.school.achievement.service.AchievementProgressService;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.UserAchievementService;
import org.springframework.stereotype.Component;

@Component
public class WriterHandler extends AbstractEventHandler<PostEventDto> {

    public WriterHandler(AchievementService achievementService,
                         UserAchievementService userAchievementService,
                         AchievementProgressService achievementProgressService) {
        super(achievementService, userAchievementService, achievementProgressService, "Писатель");
    }
}
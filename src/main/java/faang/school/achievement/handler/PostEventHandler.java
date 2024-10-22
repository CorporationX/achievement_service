package faang.school.achievement.handler;

import faang.school.achievement.dto.PostCreatedEvent;
import faang.school.achievement.service.AchievementService;

public abstract class PostEventHandler extends AbstractEventHandler<PostCreatedEvent> {
    public PostEventHandler(AchievementService achievementService) {
        super(achievementService);
    }
}

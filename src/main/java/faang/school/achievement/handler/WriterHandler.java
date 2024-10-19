package faang.school.achievement.handler;

import faang.school.achievement.dto.PostCreatedEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.service.AchievementService;
import org.springframework.stereotype.Component;

@Component
public class WriterHandler extends PostEventHandler {
    private static final String ACHIEVEMENT_TITLE = "WRITER";

    public WriterHandler(AchievementService achievementService) {
        super(achievementService);
    }

    @Override
    public void handleEvent(PostCreatedEvent event) {
        Achievement achievement = getAchievementByTitle(ACHIEVEMENT_TITLE);
        long achievementId = achievement.getId();
        long userId = event.getAuthorId();
        processAchievement(userId, achievementId);
    }
}

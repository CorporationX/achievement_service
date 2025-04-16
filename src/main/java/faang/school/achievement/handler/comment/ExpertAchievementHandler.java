package faang.school.achievement.handler.comment;

import faang.school.achievement.dto.CommentEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.service.AchievementService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ExpertAchievementHandler extends CommentEventHandler implements EventHandler<CommentEvent> {
    private static final String TITLE_ACHIEVEMENT = "EXPERT";

    public ExpertAchievementHandler(AchievementService achievementService) {
        super(achievementService);
    }

    @Override
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleEvent(CommentEvent event) {
        processAchievement(event, TITLE_ACHIEVEMENT);
    }
}

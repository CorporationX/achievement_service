package faang.school.achievement.eventhandler;

import faang.school.achievement.dto.comment.CommentEvent;
import faang.school.achievement.service.AchievementServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
public abstract class CommentEventHandler implements EventHandler<CommentEvent> {

    private final AchievementServiceImpl achievementService;
    private final String achievementTitle;

    @Async
    @Override
    public void handle(CommentEvent commentEvent) {
        achievementService.operationAchievement(commentEvent.authorId(), achievementTitle);
    }
}
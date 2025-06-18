package faang.school.achievement.handler.comment;

import faang.school.achievement.dto.event.CommentEventDto;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Async
@Component
@RequiredArgsConstructor
public class ExpertAchievementHandler extends CommentEventHandler {

    private static final String ACHIEVEMENT_TITLE = "EXPERT";

    private final AchievementService achievementService;

    @Override
    protected void process(CommentEventDto event) {
        long userId = event.getAuthorId();

        if (achievementService.hasAchievement(userId, ACHIEVEMENT_TITLE)) {
            return;
        }
        achievementService.incrementProgress(userId, ACHIEVEMENT_TITLE);
    }
}

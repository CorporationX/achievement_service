package faang.school.achievement.handler;

import faang.school.achievement.dto.CommentEventDto;
import faang.school.achievement.model.AchievementTitle;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EvilCommenterAchievementHandler {
    private final AchievementService achievementService;
    @Value("${achievement.evil_commenter.points}")
    private long evilCommenterPoints;

    @Async("threadPool")
    public void computeAchievement(CommentEventDto commentEventDto) {
        Long userId = commentEventDto.getCommentAuthorId();
        if (!achievementService.hasAchievement(userId, AchievementTitle.EVIL_COMMENTER)) {
            achievementService.updateProgress(userId, AchievementTitle.EVIL_COMMENTER, evilCommenterPoints);
        }
    }
}

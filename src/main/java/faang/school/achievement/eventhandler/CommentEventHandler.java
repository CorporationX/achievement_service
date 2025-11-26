package faang.school.achievement.eventhandler;

import faang.school.achievement.dto.comment.CommentEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.service.AchievementService;
import org.springframework.scheduling.annotation.Async;

public abstract class CommentEventHandler implements EventHandler<CommentEvent> {

    private final AchievementService achievementService;
    private final String achievementTitle;

    protected CommentEventHandler(AchievementService achievementService, String achievementTitle) {
        this.achievementService = achievementService;
        this.achievementTitle = achievementTitle;
    }

    @Async
    @Override
    public void handle(CommentEvent commentEvent) {
        Achievement achievement = achievementService.getAchievementByTitle(achievementTitle);
        boolean isAchievementExist = achievementService.hasAchievement(commentEvent.authorId(), achievement.getId());
        if (!isAchievementExist) {
            achievementService.createProgressIfNecessary(commentEvent.authorId(), achievement.getId());
            achievementService.incrementAndSaveProgress(commentEvent.authorId(), achievement.getId());
            long currentProgress = achievementService.getProgress(commentEvent.authorId(), achievement.getId());

            if (currentProgress >= achievement.getPoints()) {
                achievementService.giveAchievement(commentEvent.authorId(), achievement);
            }
        }
    }
}







package faang.school.achievement.handler;


import faang.school.achievement.dto.event.CommentEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpertAchievementHandler implements EventHandler {
    private static final String ACHIEVEMENT_NAME = "EXPERT";
    private static final int REQUIRED_POINTS = 1000;

    private final AchievementService achievementService;

    @Override
    @Async
    public void handle(CommentEvent event) {
        Long userId = event.getAuthorId();

        Achievement achievement = achievementService.getAchievementByName(ACHIEVEMENT_NAME);
        Long achievementId = achievement.getId();

        if (achievementService.hasAchievement(userId, achievementId)) {
            return;
        }

        achievementService.createProgressIfNecessary(userId, achievementId);

        AchievementProgress progress = achievementService.getProgress(userId, achievementId);
        progress.increment();
        achievementService.saveProgress(progress);

        if (progress.getCurrentPoints() >= REQUIRED_POINTS) {
            achievementService.giveAchievement(userId, achievementId);
        }

    }
}

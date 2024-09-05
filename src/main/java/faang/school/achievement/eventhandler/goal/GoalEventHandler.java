package faang.school.achievement.eventhandler.goal;

import faang.school.achievement.event.goal.GoalSetEvent;
import faang.school.achievement.eventhandler.EventHandler;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.achievement.AchievementService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;


@RequiredArgsConstructor
public class GoalEventHandler implements EventHandler<GoalSetEvent> {
    private final AchievementService achievementService;
    private final AchievementCache achievementCache;
    private final String achievementName;

    @Override
    @Async("goalSetEventPool")
    @Transactional
    public void handle(GoalSetEvent event) {
        Achievement achievement = achievementCache.get(achievementName);
        long achievementId = achievement.getId();
        long userId = event.getUserId();
        if (!achievementService.hasAchievement(userId, achievementId)) {
            achievementService.createProgressIfNecessary(userId, achievementId);
            AchievementProgress progress = achievementService.getProgress(userId, achievementId);
            progress.increment();
            achievementService.updateProgress(progress);
            if (progress.getCurrentPoints() >= achievement.getPoints()) {
                achievementService.giveAchievement(userId, achievement);
            }
        }
    }
}
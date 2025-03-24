package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.messaging.CommentStartEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExpertAchievementHandler extends EventHandler<CommentStartEvent> {

    public static final String ACHIEVEMENT_NAME = "EXPERT";

    public ExpertAchievementHandler(AchievementService achievementService, AchievementCache achievementCache) {
        super(achievementService, achievementCache);
    }

    @Override
    @Async("fixedThreadPool")
    public void handle(CommentStartEvent event) {
        Achievement achievement = achievementCache.get(ACHIEVEMENT_NAME);
        long achievementId = achievement.getId();
        long userId = event.userId();
        if (!achievementService.hasAchievement(userId, achievementId)) {
            achievementService.createProgressIfNecessary(userId, achievementId);
            AchievementProgress progress = achievementService.getProgress(userId, achievementId);
            progress.increment();
            if (progress.getCurrentPoints() == achievement.getPoints()) {
                achievementService.giveAchievement(userId, achievementId);
            }
            achievementService.saveProgress(progress);
        }
    }
}

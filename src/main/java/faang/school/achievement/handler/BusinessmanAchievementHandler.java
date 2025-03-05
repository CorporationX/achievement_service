package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.event.project.ProjectEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BusinessmanAchievementHandler extends EventHandler<ProjectEvent> {
    public final static String ACHIEVEMENT_NAME = "BUSINESSMAN";

    public BusinessmanAchievementHandler(AchievementService achievementService,
                                         AchievementCache achievementCache
    ) {
        super(achievementService, achievementCache);
    }

    @Async("fixedThreadPool")
    @Transactional
    public void handle(ProjectEvent event) {
        Achievement achievement = achievementCache.get(ACHIEVEMENT_NAME);
        long userId = event.getUserId();
        long achievementId = achievement.getId();
        if (achievementService.hasAchievement(userId, achievementId)) {
            return;
        }

        achievementService.createProgressIfNecessary(userId, achievementId);
        AchievementProgress achievementProgress = achievementService
                .getProgress(userId, achievementId);
        achievementProgress.increment();

        if (achievementProgress.getCurrentPoints() >= achievement.getPoints()) {
            achievementService.giveAchievement(achievementId, achievement);
        }
        achievementService.saveProgress(achievementProgress);
    }
}

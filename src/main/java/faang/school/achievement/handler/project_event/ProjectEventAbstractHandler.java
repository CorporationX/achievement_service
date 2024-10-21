package faang.school.achievement.handler.project_event;


import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.ProjectEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
@Slf4j
public abstract class ProjectEventAbstractHandler implements ProjectEventHandler {
    private final AchievementService achievementService;
    private final AchievementCache cache;
    private final String achievementTitle;

    @Override
    @Async("taskExecutor")
    public void handle(ProjectEvent event) {
        Achievement cachedAchievement = getAchievementFromCache();
        Long achievementId = cachedAchievement.getId();
        Long userId = event.getAuthorId();
        boolean isHaveAchievement = achievementService.hasAchievement(userId, achievementId);
        if (isHaveAchievement) {
            return;
        }

        achievementService.createProgressIfNecessary(userId, achievementId);
        AchievementProgress achievementProgress = achievementService.getProgress(userId, achievementId);
        achievementProgress.increment();
        achievementService.updateProgress(achievementProgress);
        log.info("Saved progress: userId = {}, achievementId  = {}, points = {}",
            userId,achievementId, achievementProgress.getCurrentPoints()
        );

        if (achievementProgress.getCurrentPoints() >= cachedAchievement.getPoints()) {
            achievementService.giveAchievement(userId, achievementId);
        }
    }

    protected Achievement getAchievementFromCache() {
        return cache.getByTitle(achievementTitle);
    }
}

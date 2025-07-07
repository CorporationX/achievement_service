package faang.school.achievement.achievement_handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
public abstract class AbstractEventHandler<T> {
    private final AchievementCache cache;
    private final AchievementService service;
    private final String achievementName;
    private final int requiredProgress;

    @Async
    public void proceedAchievement(long userId) {
        Achievement achievement = cache.getByName(achievementName);
        long achievementId = achievement.getId();
        if(!service.hasAchievement(userId, achievementId)) {
            service.createProgressIfNecessary(userId, achievementId);
            AchievementProgress progress = service.getProgress(userId, achievementId);
            progress.increment();
            if(progress.getCurrentPoints() == requiredProgress) {
                service.giveAchievement(userId, achievementId);
            }
        }
    }
}

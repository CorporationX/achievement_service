package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCacheRedis;
import faang.school.achievement.event.Event;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
public abstract class ProjectEventHandler<T extends Event> implements EventHandler<T> {
    private final AchievementService achievementService;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementCacheRedis achievementCache;
    private final String title;

    @Async
    public void handleEvent(Event event) {
        long userId = event.getUserId();
        Achievement achievement = achievementCache.getAchievementByTitle(title);
        if (!achievementService.hasAchievement(userId, achievement.getId())) {
            achievementService.createProgressIfNecessary(userId, achievement.getId());
            AchievementProgress achievementProgress = achievementService
                    .getProgress(userId, achievement.getId());
            achievementProgress.increment();
            achievementProgressRepository.save(achievementProgress);

            if (achievementProgress.getCurrentPoints() >= achievement.getPoints()) {
                achievementService.giveAchievement(userId, achievement);
            }
        }
    }
}

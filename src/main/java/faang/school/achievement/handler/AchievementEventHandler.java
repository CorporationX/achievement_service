package faang.school.achievement.handler;

import faang.school.achievement.dto.Event;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.achievement.AchievementService;
import faang.school.achievement.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public abstract class AchievementEventHandler<T extends Event> extends EventHandler<T> {

    private final CacheService<Achievement> achievementCacheService;
    private final AchievementService achievementService;

    public AchievementEventHandler(CacheService<String> cacheService,
                                   CacheService<Achievement> achievementCacheService,
                                   AchievementService achievementService) {
        super(cacheService);
        this.achievementCacheService = achievementCacheService;
        this.achievementService = achievementService;
    }

    @Override
    protected void handleEvent(T event) {
        log.info("Handling event: {}", event);
        String achievementName = getAchievementName();
        Achievement achievement = achievementCacheService.get(achievementName, Achievement.class);
        long userId = getUserIdFromEvent(event), achievementId = achievement.getId();

        if (!achievementService.hasAchievement(userId, achievementId)) {
            achievementService.createProgressIfNecessary(userId, achievementId);
            AchievementProgress achievementProgress = achievementService.getProgress(userId, achievementId);
            achievementProgress.increment();
            tryGiveAchievement(achievementProgress, achievement, userId);
        } else {
            log.info("User {} has already achievement with id {}", userId, achievementId);
        }
    }

    private void tryGiveAchievement(AchievementProgress achievementProgress, Achievement achievement, long userId) {
        if (achievementProgress.getCurrentPoints() >= achievement.getPoints()) {
            log.info("Achievement was successfully issued to the user {}", userId);
            achievementService.giveAchievement(userId, achievement);
        } else {
            log.info("User {} has not yet reached the required number of points to achieve the achievement {}",
                    userId, achievement.getId());
        }
    }

    protected abstract String getAchievementName();
    protected abstract long getUserIdFromEvent(T event);
}

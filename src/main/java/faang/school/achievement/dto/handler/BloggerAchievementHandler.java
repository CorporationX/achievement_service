package faang.school.achievement.dto.handler;

import faang.school.achievement.dto.FollowerEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BloggerAchievementHandler extends EventHandler<FollowerEvent> {

    private static final String ACHIEVEMENT_NAME = "BLOGGER";
    private static final String ACHIEVEMENTS_CACHE_NAME = "achievements";

    private final CacheService<Achievement> achievementCacheService;
    private final AchievementService achievementService;

    public BloggerAchievementHandler(CacheService<String> cacheService,
                                     CacheService<Achievement> achievementCacheService,
                                     AchievementService achievementService) {
        super(cacheService);
        this.achievementCacheService = achievementCacheService;
        this.achievementService = achievementService;
    }

    @Override
    protected void handleEvent(FollowerEvent event) {
        log.info("Handling follower event: {}", event);
        Achievement achievement = achievementCacheService.getFromMap(ACHIEVEMENTS_CACHE_NAME, ACHIEVEMENT_NAME, Achievement.class);
        long userId = event.getUserId(), achievementId = achievement.getId();

        if (!achievementService.hasAchievement(userId, achievementId)) {
            achievementService.createProgressIfNecessary(userId, achievementId);
            AchievementProgress achievementProgress = achievementService.getProgress(userId, achievementId);
            achievementProgress.increment();
            tryGiveAchievement(achievementProgress, achievement, userId);
        } else {
            log.info("User {} has already achievement with id {}", userId, achievementId);
        }
    }

    @Override
    public Class<FollowerEvent> getEventClass() {
        return FollowerEvent.class;
    }

    @Override
    protected Class<?> getHandlerClass() {
        return this.getClass();
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
}

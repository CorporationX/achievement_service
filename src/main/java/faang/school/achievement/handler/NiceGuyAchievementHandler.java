package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.events.RecommendationEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.AchievementService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.beans.EventHandler;
import java.util.NoSuchElementException;

@Component
public class NiceGuyAchievementHandler extends EventHandler {
    private static final String ACHIEVEMENT_NAME = "NICE GUY";
    private final AchievementService achievementService;
    private final AchievementCache achievementCache;

    public NiceGuyAchievementHandler(Object target,
                                     String action,
                                     String eventPropertyName,
                                     String listenerMethodName,
                                     AchievementService achievementService,
                                     AchievementCache achievementCache) {
        super(target, action, eventPropertyName, listenerMethodName);
        this.achievementService = achievementService;
        this.achievementCache = achievementCache;
    }

    @Async("taskExecutor")
    public void handleEvent(RecommendationEvent recommendationEvent) {
        Achievement achievement = achievementCache.get(ACHIEVEMENT_NAME).orElseThrow(
                () -> new NoSuchElementException("No achievement exists with such title."));

        boolean hasAchievement = achievementService.hasAchievement(
                recommendationEvent.receiverId(),
                achievement.getId()
        );
        if (hasAchievement) {
            throw new IllegalStateException("User already has this achievement.");
        }

        achievementService.createProgressIfNecessary(recommendationEvent.receiverId(), achievement.getId());

        AchievementProgress achievementProgress = achievementService.getProgress(
                recommendationEvent.receiverId(),
                achievement.getId()
        ).orElseThrow(() -> new NoSuchElementException("No progress present for such user and achievement"));

        // Does it increase automatically in DB? Also what about atomic?
        achievementProgress.increment();

        if (achievementProgress.getCurrentPoints() == achievement.getPoints()) {
            UserAchievement userAchievementToGive = new UserAchievement();
            userAchievementToGive.setAchievement(achievement);
            userAchievementToGive.setUserId(recommendationEvent.receiverId());
            achievementService.giveAchievement(userAchievementToGive);
        }
    }
}
package faang.school.achievement.service.event_handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public abstract class AbstractEventHandler<T> implements EventHandler<T> {
    private final AchievementCache achievementCache;
    private final AchievementService achievementService;
    private final String achievementTitle;

    @Transactional
    @Override
    public void handleEvent(T event) {
        Achievement achievement = achievementCache.get(achievementTitle);
        long userId = getUserId(event);

        if (achievementService.hasAchievement(userId, achievement.getId())) {
            return;
        }

        achievementService.createProgressIfNecessary(userId, achievement.getId());
        AchievementProgress progress = achievementService.getProgress(userId, achievement.getId());
        progress.increment();
        if (progress.getCurrentPoints() >= achievement.getPoints()) {
            achievementService.giveAchievement(userId, achievement);
        }
        achievementService.saveProgress(progress);
    }

    protected abstract long getUserId(T event);
}

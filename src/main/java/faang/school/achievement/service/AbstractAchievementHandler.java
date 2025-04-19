package faang.school.achievement.service;

import faang.school.achievement.dto.Event;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
public abstract class AbstractAchievementHandler<T extends Event> implements EventHandler<T> {
    protected final AchievementService achievementService;
    private final String achievementTitle;

    @Async
    @Override
    public void handle(T event) {
        Achievement achievement = achievementService.getAchievementByTitle(achievementTitle);

        if (achievement == null) {
            return;
        }

        Long userId = getUserId(event);
        if (achievementService.hasAchievement(userId, achievement.getId())) {
            return;
        }

        AchievementProgress achievementProgress =
                achievementService.getOrCreateProgress(userId, achievement.getId());

        achievementProgress.increment();

        achievementService.updateProgress(achievementProgress);

        if (achievementProgress.getCurrentPoints() >= achievement.getPoints()) {
            achievementService.giveAchievement(userId, achievement.getId());
        }
    }

    protected abstract Long getUserId(T event);
}

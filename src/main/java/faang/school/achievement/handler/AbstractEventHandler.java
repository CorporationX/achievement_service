package faang.school.achievement.handler;

import faang.school.achievement.cashe.AchievementCache;
import faang.school.achievement.exception.EventHandlingException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public abstract class AbstractEventHandler<T> implements EventHandler<T> {
    private final AchievementCache achievementCache;
    private final AchievementService achievementService;
    private final String achievementTitle;

    @Async("threadPool")
    @Transactional
    @Override
    public void handle(T event) {
        try {
            Achievement achievement = achievementCache.get(achievementTitle);
            long userId = getUserId(event);

            if (achievementService.hasAchievement(userId, achievement.getId())) {
                return;
            }

            achievementService.createProgress(userId, achievement.getId());
            AchievementProgress progress = achievementService.getProgress(userId, achievement.getId());
            progress.increment();
            if (progress.getCurrentPoints() >= achievement.getPoints()) {
                achievementService.giveAchievement(userId, achievement.getTitle());
            }
            achievementService.saveProgress(progress);
        } catch (Exception e) {
            String errorMessage = "Ошибка при обработке ивента";
            throw new EventHandlingException(errorMessage);
        }
    }

    protected abstract long getUserId(T event);
}
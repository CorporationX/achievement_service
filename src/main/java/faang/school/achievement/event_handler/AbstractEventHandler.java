package faang.school.achievement.event_handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.exception.EventHandlingException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventHandler<T> implements EventHandler<T> {
    private final AchievementCache achievementCache;
    private final AchievementService achievementService;
    private final String achievementTitle;

    @Async("threadPool")
    @Transactional
    @Override
    public void handleEvent(@NotNull T event) {
        try {
            Achievement achievement = achievementCache.get(achievementTitle);
            long userId = getUserId(event);

            if (achievementService.hasAchievement(userId, achievement.getId())) {
                log.info("Достижение {} у пользователя с id {} уже есть", achievement.getTitle(), userId);
                return;
            }

            achievementService.createProgressIfNecessary(userId, achievement.getId());
            AchievementProgress progress = achievementService.getProgress(userId, achievement.getId());
            progress.increment();
            if (progress.getCurrentPoints() >= achievement.getPoints()) {
                achievementService.giveAchievementIfNecessary(userId, achievement);
            }
            achievementService.saveProgress(progress);
        } catch (Exception e) {
            String errorMessage = "Ошибка при обработке ивента";
            log.error("{} {} : {}", errorMessage, event.getClass().getSimpleName(), e.getMessage(), e);
            throw new EventHandlingException(errorMessage);
        }
    }

    protected abstract long getUserId(T event);
}

package faang.school.achievement.handler.like;

import faang.school.achievement.dto.error.LikeEvent;
import faang.school.achievement.exception.AchievementDoesntExistsException;
import faang.school.achievement.exception.ProgressNotFoundException;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public abstract class LikeEventHandler implements EventHandler<LikeEvent> {
    private final AchievementService achievementService;

    protected abstract String getAchievementName();

    @Override
    @Async("likeHandleAsync")
    public void handleEvent(LikeEvent event) {
        Achievement achievement = achievementService.getAchievementByTitle(getAchievementName()).orElseThrow(
                () -> new AchievementDoesntExistsException(
                        "Достижение %s не найдено", getAchievementName()));
        Long userId = event.getAuthorId();
        Long achievementId = achievement.getId();

        if (achievementService.hasAchievementForUser(userId, achievementId)) {
            log.debug("Пользователь с id {} уже имеет достижение {}", event.getAuthorId(), achievement.getTitle());
            return;
        }

        achievementService.createProgressIfNecessary(userId, achievementId);

        AchievementProgress progress = achievementService.getAchievementProgress(userId, achievementId)
                .orElseThrow(() -> new ProgressNotFoundException(
                        "Пользователь с айди %d не выполнил прогресс достижения: %s",
                        userId, achievement.getTitle()));

        long newPoints = achievementService.incrementProgress(progress.getId()) + progress.getCurrentPoints();

        if (newPoints >= achievement.getPoints()) {
            achievementService.giveAchievementForUser(userId, achievement);
            log.info("Пользователь с id {} получил новое достижение: {}}",
                    userId, achievement.getTitle());
        }
    }

}

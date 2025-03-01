package faang.school.achievement.handler;

import faang.school.achievement.event.TaskCompletedEvent;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MrProductivityAchievementHandler implements EventHandler<TaskCompletedEvent> {

    @Value("${achievements.MrProductivity.requiredPoints}")
    private long requiredPoints;

    @Value("${achievements.MrProductivity.title}")
    private String title;

    private final AchievementService achievementService;

    @Async
    @Transactional
    @Override
    public void handle(TaskCompletedEvent event) {
        Long userId = event.getUserId();
        Achievement achievement = achievementService.getAchievementByTitle(title);
        Long achievementId = achievement.getId();

        if (achievementService.hasAchievement(userId, achievementId)) {
            log.info("Пользователь {} уже имеет ачивку '{}', пропуск", userId, title);
            return;
        }

        AchievementProgress progress = updateAchievementProgress(userId, achievement, achievementId);
        log.info("Обновленный прогресс для пользователя {}: {}/{}", userId, progress.getCurrentPoints(), requiredPoints);

        if (isAchievementCompleted(progress)) {
            grantAchievement(userId, achievementId);
            log.info("Пользователь {} получил ачивку '{}'", userId, title);
        }
    }

    private AchievementProgress updateAchievementProgress(Long userId, Achievement achievement, Long achievementId) {
        achievementService.createProgressIfNecessary(userId, achievementId);
        AchievementProgress progress = achievementService.getProgress(userId, achievementId);
        progress.increaseByNumber(achievement.getPoints());
        return progress;
    }

    private boolean isAchievementCompleted(AchievementProgress progress) {
        return progress.getCurrentPoints() >= requiredPoints;
    }

    private void grantAchievement(Long userId, Long achievementId) {
        achievementService.giveAchievement(userId, achievementId);
    }
}
package faang.school.achievement.handler;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.adapter.AchievementRepositoryAdapter;
import faang.school.achievement.service.AchievementService;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractAchievementHandler<E> implements EventHandler<E> {
    private final AchievementService achievementService;
    private final AchievementRepositoryAdapter achievementRepositoryAdapter;

    private final String achievementTitle;

    @Async("achievementHandlerExecutor")
    @Retryable(retryFor = OptimisticLockException.class, backoff = @Backoff(delay = 1000, multiplier = 5))
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void handle(E event) {
        Achievement achievement = getAchievementByTitle();
        long userId = getUserId(event);

        long achievementId = achievement.getId();

        if (achievementService.hasAchievement(userId, achievementId)) {
            return;
        }

        achievementService.createProgressIfNecessary(userId, achievementId);

        AchievementProgress achievementProgress = achievementService.getProgress(userId, achievementId);
        achievementProgress.increment();

        if (achievement.getPoints() == achievementProgress.getCurrentPoints()) {
            achievementService.giveAchievement(userId, achievement);
        }
    }

    public abstract long getUserId(E event);

    private Achievement getAchievementByTitle() {
        return achievementRepositoryAdapter.getByTitle(achievementTitle);
    }
}

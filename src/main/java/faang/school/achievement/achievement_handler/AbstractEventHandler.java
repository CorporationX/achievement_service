package faang.school.achievement.achievement_handler;

import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
public abstract class AbstractEventHandler {
    private final AchievementService service;
    private final String achievementName;
    private final int requiredProgress;
    private final AchievementRepository achievementRepository;

    @Async
    public void proceedAchievement(long userId) {
        Achievement achievement = achievementRepository
                .findByTitle(achievementName)
                .orElseThrow(() -> new NoSuchElementException("Couldn't find achievement with name: " + achievementName)
                );
        long achievementId = achievement.getId();
        if (!service.hasUserAchievement(userId, achievementId)) {
            service.createProgressIfNecessary(userId, achievementId);
            AchievementProgress progress = service.getProgress(userId, achievementId);
            progress.increment();
            if (progress.getCurrentPoints() == requiredProgress) {
                service.giveAchievement(userId, achievementId);
            }
        }
    }
}

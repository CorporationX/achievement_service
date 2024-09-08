package faang.school.achievement.eventhandler;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.achievement.AchievementService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractAchievementHandler<E> implements EventHandler<E> {
    private final AchievementService achievementService;
    private final AchievementCache cache;
    private final String achievementName;

    @Override
    public void handle(E event) {
        Achievement achievement = getAchievement(achievementName);
        long achievementId = achievement.getId();
        long userId = getAchievingUserId(event);

        boolean userHasAchievement = achievementService.hasAchievement(userId, achievementId);

        if (userHasAchievement) {
            return;
        }

        AchievementProgress progress = modifyAchievementProgress(achievementId, userId);

        if (areConditionsAchievementPerformed(achievement, progress)) {
            giveAchievementToUser(achievement, userId);
        }

    }

    protected Achievement getAchievement(String achievementName) {
        return cache.get(achievementName);
    }

    protected abstract long getAchievingUserId(E event);

    protected AchievementProgress modifyAchievementProgress(long achievementId, long userId) {
        achievementService.createProgressIfNecessary(userId, achievementId);
        AchievementProgress progress = achievementService.getProgress(userId, achievementId);
        progress.increment();
        achievementService.updateProgress(progress);

        return progress;
    }

    protected boolean areConditionsAchievementPerformed(Achievement achievement, AchievementProgress progress) {
        return progress.getCurrentPoints() >= achievement.getPoints();
    }

    protected void giveAchievementToUser(Achievement achievement, long userId) {
        achievementService.giveAchievement(userId, achievement);
    }
}

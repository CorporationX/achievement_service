package faang.school.achievement.handler;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.AchievementProgressService;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.UserAchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
public class AbstractEventHandler<T> implements EventHandler<T> {

    private final AchievementService achievementService;
    private final UserAchievementService userAchievementService;
    private final AchievementProgressService achievementProgressService;
    private final String title;

    @Override
    @Async
    public void handle(Long userId) {
        Achievement achievement = achievementService.getAchievement(title);
        long achievementId = achievement.getId();
        if (!userAchievementService.userAchievementExists(userId, achievementId)) {
            AchievementProgress progress = achievementProgressService.getProgress(userId, achievementId);
            progress.increment();
            achievementProgressService.updateProgress(progress);

            giveAchievement(userId, progress, achievement);
        }
    }

    public void giveAchievement(long userId, AchievementProgress progress, Achievement achievement) {
        if (progress.getCurrentPoints() == achievement.getPoints()) {
            UserAchievement userAchievement = UserAchievement.builder()
                    .achievement(achievement)
                    .userId(userId)
                    .build();
            userAchievementService.saveAchievement(userAchievement);
        }
    }
}
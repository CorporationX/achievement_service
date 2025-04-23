package faang.school.achievement.handler.like;

import faang.school.achievement.dto.LikeEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class LikeEventHandler implements EventHandler<LikeEvent> {
    private static final String INFO_GOT_LIKE = "User {} got a like. Progress for '{}': {}/{}";
    protected final AchievementServiceImpl achievementService;

    protected abstract String getAchievementName();
    protected abstract int getRequiredLikes();

    @Override
    public void handle(LikeEvent event) {
        Long userId = event.getAuthorId();
        Achievement achievement = achievementService.getAchievementByTitle(getAchievementName());

        if(achievementService.hasAchievement(userId, achievement.getId())) {
            return;
        }

        achievementService.createProgressIfNecessary(userId, achievement.getId());
        AchievementProgress achievementProgress = achievementService.getProgress(userId, achievement.getId());
        long currentProgress = achievementService.incrementProgress(achievementProgress);
        if(currentProgress >= getRequiredLikes()) {
            achievementService.giveAchievement(achievementProgress);
            log.info(INFO_GOT_LIKE,
                    userId, getAchievementName(), currentProgress, getRequiredLikes());
        }
    }
}

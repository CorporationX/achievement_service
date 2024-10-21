package faang.school.achievement.eventHandler;

import faang.school.achievement.dto.FollowerEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
public class FollowersAchievementHandler implements EventHandler<FollowerEvent>{

    private final AchievementService achievementService;

    @Async
    public void handle(FollowerEvent followerEvent) {
        Achievement achievement = new Achievement(); // = achievementCach.get
        long userId = followerEvent.getFolloweeId();
        long achievementId = achievement.getId();
        boolean hasAchievement = achievementService.hasAchievement(userId, achievementId);

        if (hasAchievement) {
            AchievementProgress achievementProgress = achievementService.getProgress(userId, achievementId);
            achievementProgress.increment();
            if (achievement.getPoints() == achievementProgress.getCurrentPoints()) {
                achievementService.giveAchievement(achievement, userId);
            }
        }
    }

}

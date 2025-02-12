package faang.school.achievement.handler.blogger;

import faang.school.achievement.servce.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BloggerAchievementHandler implements EventHandler<FollowerEvent> {

    private final AchievementService achievementService;

    @Async
    @Override
    public void handle(FollowerEvent event) {
        Long userId = event.getFolloweeId();

        if (achievementService.hasAchievement(userId, "Blogger")) {
            return;
        }

        achievementService.incrementProgress(userId, "Blogger");
    }
}

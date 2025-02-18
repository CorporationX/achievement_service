package faang.school.achievement.handler.follower;

import faang.school.achievement.event.follower.FollowEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CelebrityAchievementHandler extends EventHandler<FollowEvent> {
    private final static String ACHIEVEMENT_NAME = "CELEBRITY";

    private final UserService userService;

    public CelebrityAchievementHandler(
            AchievementService achievementService,
            UserService userService
    ) {
        super(achievementService);
        this.userService = userService;
    }

    @Async
    public void handle(FollowEvent event) {
        var achievement = achievementService.getAchievementByTitle(ACHIEVEMENT_NAME);
        var userDto = userService.getUserById(event.getFolloweeId(), event.getFollowerId());
        if (achievementService.hasAchievement(userDto.id(), achievement.getId())) {
            return;
        }

        var achievementProgress = achievementService.createProgressIfNecessaryAndReturn(
                userDto.id(),
                achievement.getId()
        );
        achievementProgress.increment();

        if (achievementProgress.getCurrentPoints() >= achievement.getPoints()) {
            achievementService.giveAchievement(achievement.getId(), userDto.id());
            log.info("Пользователь {} получил достижение {}", userDto.id(), ACHIEVEMENT_NAME);
        }

        achievementService.saveProgress(achievementProgress);
    }
}

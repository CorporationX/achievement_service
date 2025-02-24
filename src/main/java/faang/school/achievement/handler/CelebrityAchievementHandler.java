package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.event.follower.FollowEvent;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CelebrityAchievementHandler extends EventHandler<FollowEvent> {
    public final static String ACHIEVEMENT_NAME = "CELEBRITY";

    private final UserService userService;

    public CelebrityAchievementHandler(
            AchievementService achievementService,
            AchievementCache achievementCache,
            UserService userService
    ) {
        super(achievementService, achievementCache);
        this.userService = userService;
    }


    @Async
    @Transactional
    public void handle(FollowEvent event) {
        var achievement = achievementCache.get(ACHIEVEMENT_NAME);
        var userDto = userService.getUserById(event.getFolloweeId(), event.getFollowerId());
        long userId = userDto.id();
        long achievementId = achievement.getId();
        if (achievementService.hasAchievement(userId, achievementId)) {
            return;
        }

        achievementService.createProgressIfNecessary(userId, achievementId);
        var achievementProgress = achievementService.getProgress(userId, achievementId);
        achievementProgress.increment();

        if (achievementProgress.getCurrentPoints() >= achievement.getPoints()) {
            achievementService.giveAchieve(achievement.getId(), userDto.id());
            log.info("Пользователь {} получил достижение {}", userDto.id(), ACHIEVEMENT_NAME);
        }

        achievementService.saveProgress(achievementProgress);
    }
}

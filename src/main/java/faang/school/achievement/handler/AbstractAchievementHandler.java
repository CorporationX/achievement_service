package faang.school.achievement.handler;


import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractAchievementHandler<T> implements EventHandler<T> {
    private final AchievementService achievementService;

    public void handleCommonEvent(Object event, long achievementId) {
        long userId = ((faang.school.achievement.event.FollowerEvent) event).followerId();

        if (!achievementService.hasAchievement(userId, achievementId)) {
            achievementService.createProgressIfNecessary(userId, achievementId);
            log.info("Create progress");

            log.info("Get progress");
            AchievementProgressDto achievementProgressDto = achievementService.incrementProgress(userId, achievementId);
            log.info("Increment progress");
            long currentPoints = achievementProgressDto.currentPoints();
            log.info("Progress points : {}", achievementProgressDto.currentPoints());
            long achievementPoints = achievementService.getAchievement(achievementId).points();
            log.info("Progress points for achievement: {}", achievementPoints);
            if (currentPoints >= achievementPoints) {
                achievementService.giveAchievement(userId, achievementId);
                log.info("User {} got new achievement {}", userId, achievementId);
            }
        }

    }

}

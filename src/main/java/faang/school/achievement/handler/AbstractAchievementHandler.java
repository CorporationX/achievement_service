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
            AchievementProgressDto achievementProgressDto = achievementService.incrementProgress(userId, achievementId);
            long currentPoints = achievementProgressDto.currentPoints();
            long achievementPoints = achievementService.getAchievement(achievementId).points();
            if (currentPoints >= achievementPoints) {
                achievementService.giveAchievement(userId, achievementId);
                log.info("User {} has got new achievement {}", userId, achievementId);
            }
        }
    }
}

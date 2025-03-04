package faang.school.achievement.handler;


import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractAchievementHandler<T> implements EventHandler<T> {

    private final Class<T> eventType;
    private final AchievementService achievementService;

    public void handleCommonEvent(long userId, String achievementTitle) {
        AchievementDto achievementDto = achievementService.getAchievement(achievementTitle);

        if (!achievementService.hasAchievement(userId, achievementDto.id())) {
            achievementService.createProgressIfNecessary(userId, achievementDto.id());
            long currentPoints = achievementService.incrementProgress(userId, achievementDto.id());

            long achievementPoints = achievementService.getAchievement(achievementDto.id()).points();
            if (currentPoints >= achievementPoints) {
                achievementService.giveAchievement(userId, achievementDto.id());
                log.info("User {} has got new achievement {}", userId, achievementDto.id());
            }
        }
    }

    @Override
    public Class<T> getInstance() {
        return eventType;
    }
}

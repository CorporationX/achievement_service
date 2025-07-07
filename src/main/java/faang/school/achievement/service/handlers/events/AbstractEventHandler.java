package faang.school.achievement.service.handlers.events;

import faang.school.achievement.dto.AchievementProgressRecord;
import faang.school.achievement.exceptions.ObjectAlreadyExistsException;
import faang.school.achievement.kafka.events.Event;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementCode;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.redis.RedisCounterService;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.handlers.achievements.AchievementCacheSettings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventHandler implements EventHandler {
    protected final AchievementService achievementService;
    protected final RedisCounterService cacheService;

    public void handleSingleCounterCache(Event event, AchievementCacheSettings cacheSettings) {
        log.info("Handling single counter cache event: {}", event);
        long userId = event.getUserId();
        AchievementCode achievementCode = cacheSettings.achievementCode();
        Long counterThreshold = cacheSettings.counterThreshold();

        Boolean isAchievementAlreadyAssigned = cacheService.checkIfAchievementAssigned(achievementCode, userId);
        if (!isAchievementAlreadyAssigned) {
            Long counter = cacheService.incrementCounter(achievementCode, userId);
            if (Objects.equals(counter, counterThreshold)) {
                Achievement achievement = achievementService.getAchievementByCode(achievementCode);
                AchievementProgress achievementProgress;
                try {
                    achievementProgress = achievementService.saveAchievementProgress(new AchievementProgressRecord(
                            achievement, userId, counter));
                } catch (ObjectAlreadyExistsException e) {
                    return;
                }
                boolean isAchievementCompleted = achievementService.assignAchievementIfCompleted(
                        new AchievementProgressRecord(achievementProgress.getAchievement(),
                                achievementProgress.getUserId(),
                                achievementProgress.getCurrentPoints()));
                if (isAchievementCompleted) {
                    cacheService.assignAchievement(achievementCode, userId);
                } else {
                    cacheService.decrementCounter(achievementCode, userId, achievementProgress.getCurrentPoints());
                }
            }
        }
    }
}
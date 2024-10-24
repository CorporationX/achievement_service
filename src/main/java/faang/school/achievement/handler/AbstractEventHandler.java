package faang.school.achievement.handler;

import faang.school.achievement.config.achievent.AchievementConfiguration;
import faang.school.achievement.config.cache.AchievementCache;
import faang.school.achievement.dto.achievement.AbstractEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.achievement.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public abstract class AbstractEventHandler<T> implements EventHandler<T> {

    protected final AchievementConfiguration achievementConfiguration;
    protected final AchievementService achievementService;
    protected final AchievementCache achievementCache;

    public void handleAchievement(AbstractEvent event,
                                  AchievementConfiguration.AchievementProp achievementProp) {
        log.info("handleAchievement() - start, event - {}", event);

        Achievement achievement = achievementCache.getAchievement(achievementProp.getTitle());

        long userId = event.getUserId();
        long achievementId = achievement.getId();

        if (!achievementService.hasAchievement(userId, achievementId)) {
            AchievementProgress progress = achievementService
                    .proceedAchievementProgress(userId, achievementId);
            if (progress.getCurrentPoints() == achievementProp.getPointsToAchieve()) {
                UserAchievement userAchievement = UserAchievement.builder()
                        .achievement(achievement)
                        .userId(userId)
                        .build();
                log.debug("Giving achievement {}! To user {}!", achievement, userId);
                achievementService.giveAchievement(userAchievement);
            }
        }

        log.info("handleAchievement() - finish, event - {}", event);
    }
}

package faang.school.achievement.messaging.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.SkillAcquiredEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.AchievementService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@AllArgsConstructor
@Transactional
public abstract class SkillEventHandler implements EventHandler {

    protected String achievementName;

    private final AchievementService achievementService;
    private final AchievementCache achievementCache;

    @Async
    @Override
    public void handleEvent(SkillAcquiredEvent event) {
        Achievement achievement = achievementCache.get(achievementName);
        long userId = event.getRecipientId();
        long achievementId = achievement.getId();

        if(!achievementService.hasAchievement(userId, achievementId)) {
            achievementService.createProgressIfNecessary(userId, achievementId);
            AchievementProgress achievementProgress = achievementService.getProgress(userId, achievementId);
            achievementProgress.increment();

            if(achievementProgress.getCurrentPoints() >= achievement.getPoints()) {
                UserAchievement userAchievement = UserAchievement.builder()
                        .achievement(achievement)
                        .userId(userId)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

                achievementService.giveAchievement(userAchievement);
            }
        }
    }
}

package faang.school.achievement.service;

import faang.school.achievement.dto.SkillAcquiredEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AchievementEventService {

    private final AchievementService achievementService;

    @Transactional
    public void processAchievementAcquired(SkillAcquiredEvent event, Achievement achievement) {
        long userId = event.getRecipientId();
        long achievementId = achievement.getId();

        if(!achievementService.hasAchievement(userId, achievementId)) {
            achievementService.createProgressIfNecessary(userId, achievementId);
            AchievementProgress achievementProgress = achievementService.getProgress(userId, achievementId);
            achievementProgress.increment();
            achievementService.saveProgress(achievementProgress);

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

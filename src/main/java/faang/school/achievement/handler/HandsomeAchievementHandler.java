package faang.school.achievement.handler;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class HandsomeAchievementHandler implements EventHandler<Long> {

    private final AchievementService achievementService;

    @Value("${achievement.titles.handsome}")
    private String handsomeAchievementTitle;

    @Async
    @Override
    public void handle(Long userId) {
        try {
            Achievement achievement = achievementService.getAchievement(handsomeAchievementTitle);

            if (achievementService.hasAchievement(userId, achievement.getId())) {
                log.info("User {} already has achievement {}", userId, handsomeAchievementTitle);
                return;
            }

            achievementService.createProgressIfNecessary(userId, achievement.getId());
            AchievementProgress progress = achievementService.getProgress(userId, achievement.getId());

            achievementService.incrementProgress(progress);

            if (progress.getCurrentPoints() >= achievement.getPoints()) {
                achievementService.giveAchievement(userId, achievement.getId());
                log.info("Achievement {} given to user {}", handsomeAchievementTitle, userId);
            }
        } catch (Exception e) {
            log.error("Error handling Handsome achievement for user {}: {}", userId, e.getMessage());
        }
    }
}
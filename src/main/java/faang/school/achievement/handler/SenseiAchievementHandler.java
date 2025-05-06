package faang.school.achievement.handler;

import faang.school.achievement.event.MentorshipStartEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SenseiAchievementHandler implements EventHandler<MentorshipStartEvent> {
    private final AchievementService achievementService;

    @Value("${achievements.sensei-name}")
    private String achievementName;

    @Override
    @Async
    public void handle(MentorshipStartEvent event) {
        Long userId = event.getMentorId();
        Achievement achievement = achievementService.getAchievementByTitle(achievementName);
        Long achievementId = achievement.getId();
        log.info("Processing achievement {} for user {}", achievementId, userId);

        if (achievementService.hasAchievement(userId, achievement.getId())) {
            log.info("User {} already has achievement {}", userId, achievementId);
            return;
        }

        AchievementProgress progress = achievementService.getOrCreateProgress(userId, achievementId);
        progress.setCurrentPoints(progress.getCurrentPoints() + 1);
        achievementService.updateProgress(progress);

        if (progress.getCurrentPoints() >= achievement.getPoints()) {
            achievementService.giveAchievement(userId, achievementId);
            log.info("Achievement {} granted to user {}", achievementId, userId);
        }
    }
}

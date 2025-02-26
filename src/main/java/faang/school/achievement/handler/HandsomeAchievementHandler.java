package faang.school.achievement.handler;

import faang.school.achievement.dto.event.ProfilePicEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HandsomeAchievementHandler {
    private static final String HANDSOME_ACHIEVEMENT = "HANDSOME";
    private final AchievementService achievementService;

    @EventListener
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleProfilePicEvent(ProfilePicEvent event) {
        log.info("Handling profile pic event: {}", event);

        Achievement achievement = achievementService.getAchievement(HANDSOME_ACHIEVEMENT);
        if (achievementService.hasAchievement(event.getUserId(), achievement)) {
            log.info("User already has achievement: {}", HANDSOME_ACHIEVEMENT);
            return;
        }
        AchievementProgress achievementProgress = achievementService.createProgressIfNecessary(event.getUserId(), achievement);
        achievementProgress = achievementService.increaseAchievementProgress(achievementProgress, 2);
        if (achievementProgress.getCurrentPoints() >= achievement.getPoints()) {
            log.info("User has enough points for achievement: {}", HANDSOME_ACHIEVEMENT);
            achievementService.giveAchievement(event.getUserId(), achievement);
        }
    }
}

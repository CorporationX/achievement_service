package faang.school.achievement.handler;

import faang.school.achievement.exception.AchievementDoesntExistsException;
import faang.school.achievement.exception.ProgressNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AbstractEventHandler {

    private static final long adminUserId = 0L;

    private final AchievementService achievementService;

    public <T> void processAchievement(T event, String title, Long userId) {
        log.debug("Processing event: {} on achievement {}", event, title);

        if (userId == adminUserId) {
            log.debug("Skipping achievement processing for admin user");
            return;
        }
        Achievement achievement = achievementService.getAchievementByTitle(title).orElseThrow(
                () -> new AchievementDoesntExistsException("Achievement with title %s not found", title));

        Long achievementId = achievement.getId();

        if (achievementService.hasAchievementForUser(userId, achievementId)) {
            log.debug("User {} already has achievement {}", userId, title);
            return;
        }
        achievementService.createProgressIfNecessary(userId, achievementId);
        AchievementProgress progress = achievementService.getAchievementProgress(userId, achievementId)
                .orElseThrow(() -> new ProgressNotFoundException("User with id %d hasn't progress on achievement: %s",
                        userId, title));
        long newPoints = achievementService.incrementProgress(progress.getId()) + progress.getCurrentPoints();
        log.debug("User progress updated on {} of achievement {}", newPoints, achievementId);

        if (newPoints >= achievement.getPoints()) {
            achievementService.giveAchievementForUser(userId, achievement);
            log.info("User with id {} has new achievement: {}, rarity: {}",
                    userId, achievement.getTitle(), achievement.getRarity());
        }
    }

}

package faang.school.achievement.handler.team;

import faang.school.achievement.AchievementService;
import faang.school.achievement.dto.TeamEvent;
import faang.school.achievement.exception.AchievementNotFoundException;
import faang.school.achievement.exception.ProgressNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public abstract class TeamEventHandler {

    private final AchievementService achievementService;

    protected void processAchievement(TeamEvent event, String title) {
        log.debug("Processing event: {} on achievement {}", event, title);

        Achievement achievement = achievementService.getAchievementByTitle(title).orElseThrow(
                () -> new AchievementNotFoundException("Achievement with title %s not found", title));

        Long userId = event.creatorId();
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

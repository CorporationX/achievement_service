package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.event.TeamEvent;
import faang.school.achievement.exeption.AchievementNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Data
@RequiredArgsConstructor
public class ManagerAchievementHandler implements EventHandler<TeamEvent>{

    private final AchievementCache achievementCache;
    private final AchievementService achievementService;
    private static final String ACHIEVEMENT_NAME = "MANAGER";



    public void handle(TeamEvent event) {
        Achievement achievement = achievementService.getAchievementByName(ACHIEVEMENT_NAME);

        Long authorId = event.getAuthorId();
        Long achievementId = achievement.getId();
        String achievementTitle = achievement.getTitle();

        log.info("Starting handleEvent for authorId: {}", authorId);

        if (achievementService.hasAchievement(authorId, achievementId)) {
            log.debug("The user with ID {} already has the {} achievement.",
                    authorId, achievementTitle);
            return;
        }

        achievementService.createProgressIfNecessary(authorId, achievementId);
        AchievementProgress progress = achievementService.getProgress(authorId, achievementId);
        progress.setCurrentPoints(progress.getCurrentPoints() + 1);
        achievementService.saveProgress(progress);

        if (achievement.getPoints() == progress.getCurrentPoints()) {
            log.info("User with ID {} has now received the {} achievement.",
                    authorId, achievementTitle);
            achievementService.giveAchievement(authorId, achievementId);
        }
        log.info("Finished handleEvent for authorId: {}", authorId);
    }
}

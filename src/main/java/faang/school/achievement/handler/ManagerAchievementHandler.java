package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.event.CommentEvent;
import faang.school.achievement.dto.event.TeamEvent;
import faang.school.achievement.exeption.AchievementNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Data
public class ManagerAchievementHandler implements EventHandler<TeamEvent>{

    private final AchievementCache achievementCache;
    private final AchievementService achievementService;


    public void handle(TeamEvent event) {
        log.info("Starting handleEvent for authorId: {}", event.getAuthorId());

        Achievement achievement = getAndValidateAchievement("MANAGER");

        if (achievementService.hasAchievement(event.getAuthorId(), achievement.getId())) {
            log.debug("The user with ID {} already has the {} achievement.", event.getAuthorId(), achievement.getTitle());
            return;
        }

        achievementService.createProgressIfNecessary(event.getAuthorId(), achievement.getId());
        AchievementProgress progress = achievementService.getProgress(event.getAuthorId(), achievement.getId());
        progress.setCurrentPoints(progress.getCurrentPoints() + 1);
        achievementService.saveProgress(progress);

        if (achievement.getPoints() == progress.getCurrentPoints()) {
            log.info("User with ID {} has now received the {} achievement.", event.getAuthorId(), achievement.getTitle());
            achievementService.giveAchievement(event.getAuthorId(), achievement.getId());
        }
        log.info("Finished handleEvent for authorId: {}", event.getAuthorId());
    }

    private Achievement getAndValidateAchievement(String achievementTitle) {
        Achievement achievement = achievementCache.get(achievementTitle);

        if (achievement == null) {
            log.error("Failed to get {} achievement from cache.", achievementTitle);
            throw new AchievementNotFoundException();
        }

        return achievement;
    }
}

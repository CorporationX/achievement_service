package faang.school.achievement.service.achievement;

import faang.school.achievement.event.TeamEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class ManagerAchievementTransactionService {

    private final AchievementService achievementService;

    @Transactional
    public void processWithTransaction(TeamEvent teamEvent) {
        log.info("Getting achievement by title from achievement repository");
        Achievement achievement = achievementService
                .getAchievementByTitleWithOutUserAndProgress("MANAGER");
        long achievementId = achievement.getId();

        if (!achievementService.hasAchievement(teamEvent.getAuthorId(), achievementId)) {
            achievementService.createProgressIfNecessary(teamEvent.getAuthorId(), achievementId);
            AchievementProgress achievementProgress = achievementService
                    .getProgress(teamEvent.getAuthorId(), achievementId);

            achievementProgress.increment();
            achievementService.saveProgress(achievementProgress);
            log.info("Updated achievement progress: {}", achievementProgress.getCurrentPoints());

            if (achievementProgress.getCurrentPoints() >= achievement.getPoints()) {
                UserAchievement userAchievement = new UserAchievement();
                userAchievement.setAchievement(achievement);
                userAchievement.setUserId(teamEvent.getAuthorId());
                userAchievement.setCreatedAt(LocalDateTime.now());
                userAchievement.setUpdatedAt(LocalDateTime.now());

                achievementService.createNewUserAchievement(userAchievement);
                log.info("Achievement '{}' granted to user {}", achievement.getTitle(), teamEvent.getAuthorId());
            }
        }
    }
}


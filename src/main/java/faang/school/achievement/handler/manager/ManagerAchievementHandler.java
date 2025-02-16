package faang.school.achievement.handler.manager;

import faang.school.achievement.event.TeamEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.achievement.AchievementService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
@Slf4j
public class ManagerAchievementHandler implements ManagerHandler {

    private final AchievementService achievementService;

    @Override
    @Async
    @Transactional
    public void startHandling(TeamEvent teamEvent) {
        log.info("getting achievement by title from achievement repository");
        Achievement achievement = achievementService
                .getAchievementByTitleWithOutUserAndProgress("MANAGER");
        long achievementId = achievement.getId();

        if(!achievementService.hasAchievement(teamEvent.getAuthorId(), achievementId)){
            achievementService.createProgressIfNecessary(teamEvent.getAuthorId(), achievementId);

            AchievementProgress achievementProgress = achievementService
                    .getProgress(teamEvent.getAuthorId(), achievementId);
            achievementProgress.increment();

            if(achievementProgress.getCurrentPoints() >= achievement.getPoints()){
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

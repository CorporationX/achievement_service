package faang.school.achievement.handler.follower;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.FollowerEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.achievement.AchievementService;
import faang.school.achievement.service.achievementprogress.AchievementProgressService;
import faang.school.achievement.service.cache.AchievementCacheService;
import faang.school.achievement.service.userachievement.UserAchievementService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public abstract class FollowerEventHandler implements EventHandler<FollowerEvent> {

    protected final AchievementService achievementService;
    protected final AchievementCacheService achievementCacheService;
    protected final AchievementProgressService achievementProgressService;
    protected final UserAchievementService userAchievementService;

    @Override
    public void handleEvent(FollowerEvent event) {
        String achievementTitle = getAchievementTitle();
        AchievementDto achievementDto = achievementCacheService.getAchievement(achievementTitle);

        long userId = event.getFolloweeId();
        long achievementId = achievementDto.getId();

        if (userAchievementService.hasAchievement(userId, achievementId)) {
            log.info("{} achievement has been already received by user {}", achievementTitle, userId);
            return;
        }

        processProgress(userId, achievementDto, achievementId);
    }

    private void processProgress(long userId, AchievementDto achievementDto, long achievementId) {
        achievementProgressService.createProgressIfNecessary(userId, achievementId);

        AchievementProgress achievementProgress = achievementProgressService.getProgress(userId, achievementId);

        achievementProgress = achievementProgressService.progressIncrement(achievementProgress.getId());

        if (achievementProgress.getCurrentPoints() >= achievementDto.getPoints()) {
            Achievement achievement = achievementService.getAchievement(achievementId);
            userAchievementService.giveAchievement(userId, achievement);
        }
    }

    protected abstract String getAchievementTitle();
}

package faang.school.achievement.handler.profilepic;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.userprofile.ProfilePicEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.achievement.AchievementService;
import faang.school.achievement.service.achievementprogress.AchievementProgressService;
import faang.school.achievement.service.cache.AchievementCacheService;
import faang.school.achievement.service.userachievement.UserAchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public abstract class ProfilePicEventHandler implements EventHandler<ProfilePicEvent> {

    protected final AchievementCacheService achievementCacheService;
    protected final UserAchievementService userAchievementService;
    protected final AchievementProgressService achievementProgressService;
    protected final AchievementService achievementService;

    @Transactional
    @Override
    public void handleEvent(ProfilePicEvent event) {
        String achievementTitle = getAchievementTitle();
        log.info("Starting handle ProfilePicEvent for user {} (\"{}\" achievement)...",
                event.getUserId(), achievementTitle);
        AchievementDto achievementDto = achievementCacheService.getAchievement(achievementTitle);
        long userId = event.getUserId();
        long achievementId = achievementDto.getId();

        if (userAchievementService.hasAchievement(userId, achievementId)) {
            log.info("\"{}\" achievement has already been received by user {}", achievementTitle, userId);
            return;
        }

        achievementProgressService.createProgressIfNecessary(userId, achievementId);
        AchievementProgress achievementProgress = achievementProgressService.getProgress(userId, achievementId);
        achievementProgress.increment();

        if (achievementProgress.getCurrentPoints() == achievementDto.getPoints()) {
            Achievement achievement = achievementService.getAchievement(achievementId);
            userAchievementService.giveAchievement(userId, achievement);
        }
    }

    protected abstract String getAchievementTitle();
}

package faang.school.achievement.handler.profilepic;

import faang.school.achievement.dto.userprofile.ProfilePicEvent;
import faang.school.achievement.service.achievement.AchievementService;
import faang.school.achievement.service.achievementprogress.AchievementProgressService;
import faang.school.achievement.service.cache.AchievementCacheService;
import faang.school.achievement.service.userachievement.UserAchievementService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.StaleStateException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HandsomeAchievementHandler extends ProfilePicEventHandler {

    private static final String ACHIEVEMENT_TITLE = "HANDSOME";

    public HandsomeAchievementHandler(
            AchievementCacheService achievementCacheService,
            UserAchievementService userAchievementService,
            AchievementProgressService achievementProgressService,
            AchievementService achievementService
    ) {
        super(achievementCacheService, userAchievementService, achievementProgressService, achievementService);
    }

    @Async("handsomeAchievementPool")
    @Override
    @Retryable(
            value = {StaleStateException.class},
            maxAttemptsExpression = "#{${app.retry.handsome-achievement.max-attempts}}",
            backoff = @Backoff(delayExpression = "#{${app.retry.handsome-achievement.delay}}")
    )
    public void handleEvent(ProfilePicEvent event) {
        super.handleEvent(event);
    }

    @Override
    protected String getAchievementTitle() {
        return ACHIEVEMENT_TITLE;
    }
}

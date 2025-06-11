package faang.school.achievement.handler.follower;

import faang.school.achievement.dto.FollowerEvent;
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
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class BloggerAchievementHandler extends FollowerEventHandler {

    private static final String ACHIEVEMENT_TITLE = "BLOGGER";

    public BloggerAchievementHandler(
            AchievementService achievementService,
            AchievementCacheService achievementCacheService,
            AchievementProgressService achievementProgressService,
            UserAchievementService userAchievementService
    ) {
        super(achievementService, achievementCacheService, achievementProgressService, userAchievementService);
    }


    @Async("bloggerAchievementPool")
    @Transactional
    @Retryable(
            value = {StaleStateException.class},
            maxAttemptsExpression = "#{${app.retry.blogger-achievement.max-attempts}}",
            backoff = @Backoff(delayExpression = "#{${app.retry.blogger-achievement.delay}}")
    )
    @Override
    public void handleEvent(FollowerEvent event) {
        super.handleEvent(event);
    }

    @Override
    protected String getAchievementTitle() {
        return ACHIEVEMENT_TITLE;
    }
}

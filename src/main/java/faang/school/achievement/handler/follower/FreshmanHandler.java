package faang.school.achievement.handler.follower;

import faang.school.achievement.event.FollowerEvent;
import faang.school.achievement.handler.AbstractAchievementHandler;
import faang.school.achievement.service.AchievementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FreshmanHandler extends AbstractAchievementHandler<FollowerEvent> {

    private final static String FRESHMAN_ACHIEVEMENT_TITLE = "FRESHMAN";

    public FreshmanHandler(AchievementService achievementService) {
        super(FollowerEvent.class, achievementService);
    }

    @Override
    @Async("taskExecutor")
    public void handleEvent(FollowerEvent event) {
        long userId = event.followerId();
        log.info("Handle event {} for Freshman achievement of user {}", event, userId);
        handleCommonEvent(userId, FRESHMAN_ACHIEVEMENT_TITLE);
    }

}

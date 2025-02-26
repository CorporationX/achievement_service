package faang.school.achievement.handler.follower;

import faang.school.achievement.event.FollowerEvent;
import faang.school.achievement.handler.AbstractAchievementHandler;
import faang.school.achievement.service.AchievementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FreshmanHandler extends AbstractAchievementHandler {

    private final static long FRESHMAN_ACHIEVEMENT_ID = 9;

    public FreshmanHandler(AchievementService achievementService) {
        super(achievementService);
    }

    @Override
    @Async("taskExecutor")
    public void handleEvent(Object event) {
        log.info("Handle event {} for Freshman achievement", event);
        handleCommonEvent(event, FRESHMAN_ACHIEVEMENT_ID);
    }

    @Override
    public Class<?> getInstance() {
        return FollowerEvent.class;
    }
}

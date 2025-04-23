package faang.school.achievement.handler.like;

import faang.school.achievement.dto.LikeEvent;
import faang.school.achievement.service.AchievementServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AllLoveAchievementHandler extends LikeEventHandler {
    private static final String ACHIEVEMENT_NAME = "ALL LOVE";
    private static final int REQUIRED_LIKES = 1000;
    private static final String INFO_HANDLED_LIKE = "Handled LIKE event for '{}'";

    public AllLoveAchievementHandler(AchievementServiceImpl achievementService) {
        super(achievementService);
    }

    @Override
    protected String getAchievementName() {
        return ACHIEVEMENT_NAME;
    }

    @Override
    protected int getRequiredLikes() {
        return REQUIRED_LIKES;
    }

    @Async
    @Override
    public void handle(LikeEvent event) {
        super.handle(event);
        log.info(INFO_HANDLED_LIKE, ACHIEVEMENT_NAME);
    }
}

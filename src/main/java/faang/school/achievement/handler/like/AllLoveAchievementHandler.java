package faang.school.achievement.handler.like;

import faang.school.achievement.dto.LikeEvent;
import faang.school.achievement.service.AchievementServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AllLoveAchievementHandler extends LikeEventHandler {
    private static final String INFO_HANDLED_LIKE = "Handled LIKE event for '{}'";

    @Value("${app.achievements.allLove.name}")
    private String achievementName;

    @Value("${app.achievements.allLove.requiredLikes}")
    private int requiredLikes;
    public AllLoveAchievementHandler(AchievementServiceImpl achievementService) {
        super(achievementService);
    }

    @Override
    protected String getAchievementName() {
        return achievementName;
    }

    @Override
    protected int getRequiredLikes() {
        return requiredLikes;
    }

    @Async
    @Override
    public void handle(LikeEvent event) {
        super.handle(event);
        log.info(INFO_HANDLED_LIKE, achievementName);
    }
}
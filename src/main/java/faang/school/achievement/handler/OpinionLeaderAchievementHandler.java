package faang.school.achievement.handler;

import faang.school.achievement.cashe.AchievementCache;
import faang.school.achievement.event.PostEvent;
import faang.school.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpinionLeaderAchievementHandler extends AbstractEventHandler<PostEvent> {
    public OpinionLeaderAchievementHandler(AchievementCache achievementCache,
                                           AchievementService achievementService,
                                           @Value("${achievements.title-leader}") String leaderTitle) {
        super(achievementCache, achievementService, leaderTitle);
    }
}
package faang.school.achievement.event_handler.opinion_leader_event;

import faang.school.achievement.cashe.AchievementCache;
import faang.school.achievement.event.PostEvent;
import faang.school.achievement.event_handler.AbstractEventHandler;
import faang.school.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpinionLeaderAchievementHandler extends AbstractEventHandler<PostEvent> {
    public OpinionLeaderAchievementHandler(AchievementCache achievementCache,
                                           AchievementService achievementService,
                                           @Value("${achievements.titles.leader}") String leaderTitle) {
        super(achievementCache, achievementService, leaderTitle);
    }

    @Override
    protected long getUserId(PostEvent event) {
        return 0;
    }
}
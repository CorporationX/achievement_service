package faang.school.achievement.handler.achievement;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementEvent;
import faang.school.achievement.handler.AbstractEventHandler;
import faang.school.achievement.handler.AchievementProgressHandler;
import faang.school.achievement.service.AchievementService;
import org.springframework.stereotype.Component;

@Component
public abstract class AchievementEventHandler extends AbstractEventHandler<AchievementEvent> {

    public AchievementEventHandler(AchievementService achievementService,
                                   AchievementCache achievementCache,
                                   AchievementProgressHandler achievementProgressHandler) {
        super(achievementService, achievementCache, achievementProgressHandler);
    }

    @Override
    public void handle(AchievementEvent event) {
        handleAchievement(event.userId(), getAchievementTitle());
    }
}

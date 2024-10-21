package faang.school.achievement.handler.achievement;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.handler.AchievementProgressHandler;
import faang.school.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RecursionAchievementHandler extends AchievementEventHandler {

    @Value("${achievements.recursion.title}")
    String title;

    public RecursionAchievementHandler(AchievementService achievementService,
                                       AchievementCache achievementCache,
                                       AchievementProgressHandler achievementProgressHandler) {
        super(achievementService, achievementCache, achievementProgressHandler);
    }

    @Override
    protected String getAchievementTitle() {
        return title;
    }
}

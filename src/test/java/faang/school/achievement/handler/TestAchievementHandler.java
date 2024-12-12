package faang.school.achievement.handler;

import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementService;

public class TestAchievementHandler extends AbstractAchievementHandler<TestEvent> {

    public TestAchievementHandler(AchievementService achievementService, AchievementCache achievementCache) {
        super(achievementService, achievementCache);
    }

    @Override
    public void handle(TestEvent event) {

    }
}

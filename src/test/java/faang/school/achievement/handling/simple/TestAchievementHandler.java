package faang.school.achievement.handling.simple;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.handling.AbstractAchievementHandler;
import faang.school.achievement.service.AchievementService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
@Profile("test")
public class TestAchievementHandler extends AbstractAchievementHandler<TestEvent> {

    public TestAchievementHandler (
            AchievementService service,
            AchievementCache cachedAchievements,
            PlatformTransactionManager txManager
    ) {
        super(service, cachedAchievements, txManager, "TEST");
    }

    @Override
    protected long extractUserId(TestEvent event) {
        return event.targetUserId();
    }
}
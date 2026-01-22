package faang.school.achievement.handling;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.model.event.MentorshipStartEvent;
import faang.school.achievement.service.AchievementService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class SenseiAchievementHandler extends AbstractAchievementHandler<MentorshipStartEvent> {

    public SenseiAchievementHandler(
            AchievementService service,
            AchievementCache cachedAchievements,
            PlatformTransactionManager txManager
    ) {
        super(service, cachedAchievements, txManager, "SENSEI");
    }

    @Override
    protected long extractUserId(MentorshipStartEvent event) {
        return event.mentorId();
    }
}
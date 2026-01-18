package faang.school.achievement.handling;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.model.event.MentorshipStartEvent;
import faang.school.achievement.service.AchievementService;
import org.springframework.stereotype.Component;

@Component
public class SenseiAchievementHandler extends AbstractAchievementHandler<MentorshipStartEvent> {

    public SenseiAchievementHandler(
            AchievementService service,
            AchievementCache cachedAchievements
    ) {
        super(service, cachedAchievements, "SENSEI");
    }

    @Override
    protected long extractUserId(MentorshipStartEvent event) {
        return event.mentorId();
    }
}
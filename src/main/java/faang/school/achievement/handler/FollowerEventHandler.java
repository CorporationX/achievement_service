package faang.school.achievement.handler;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.event.FollowerEvent;
import faang.school.achievement.service.interfaces.AchievementService;
import faang.school.achievement.service.interfaces.Cache;
import org.springframework.stereotype.Component;

@Component
public class FollowerEventHandler extends AbstractEventHandler<FollowerEvent> {
    private static final String ACHIEVEMENT_TITLE = "100_SUBSCRIBERS";

    public FollowerEventHandler(Cache<AchievementDto> achievementCache, AchievementService achievementService) {
        super(achievementCache, achievementService);
    }

    @Override
    public void handleEvent(FollowerEvent event) {
        handleAchievement(event.getFolloweeId(), ACHIEVEMENT_TITLE);
    }

    @Override
    public boolean supportsEvent(Class<FollowerEvent> eventType) {
        return FollowerEvent.class.equals(eventType);
    }
}

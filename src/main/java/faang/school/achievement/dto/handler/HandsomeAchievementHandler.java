package faang.school.achievement.dto.handler;

import faang.school.achievement.dto.ProfilePicEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.CacheService;
import org.springframework.stereotype.Component;

@Component
public class HandsomeAchievementHandler extends AchievementEventHandler<ProfilePicEvent> {
    public HandsomeAchievementHandler(CacheService<String> cacheService,
                                      CacheService<Achievement> achievementCacheService,
                                      AchievementService achievementService) {
        super(cacheService, achievementCacheService, achievementService);
    }

    @Override
    protected String getAchievementName() {
        return "HANDSOME";
    }

    @Override
    protected long getUserIdFromEvent(ProfilePicEvent event) {
        return event.getUserId();
    }

    @Override
    protected Class<ProfilePicEvent> getEventClass() {
        return ProfilePicEvent.class;
    }

    @Override
    protected Class<?> getHandlerClass() {
        return this.getClass();
    }
}

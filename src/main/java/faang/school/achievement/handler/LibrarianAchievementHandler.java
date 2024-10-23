package faang.school.achievement.handler;

import faang.school.achievement.dto.AlbumCreatedEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.service.achievement.AchievementService;
import faang.school.achievement.service.CacheService;
import org.springframework.stereotype.Component;

@Component
public class LibrarianAchievementHandler extends AchievementEventHandler<AlbumCreatedEvent> {

    public LibrarianAchievementHandler(CacheService<String> cacheService,
                                       CacheService<Achievement> achievementCacheService,
                                       AchievementService achievementService) {
        super(cacheService, achievementCacheService, achievementService);
    }

    @Override
    protected String getAchievementName() {
        return "LIBRARIAN";
    }

    @Override
    protected long getUserIdFromEvent(AlbumCreatedEvent event) {
        return event.getUserId();
    }

    @Override
    protected Class<AlbumCreatedEvent> getEventClass() {
        return AlbumCreatedEvent.class;
    }

    @Override
    protected Class<?> getHandlerClass() {
        return this.getClass();
    }
}

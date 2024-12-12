package faang.school.achievement.handler.impl.comment;

import faang.school.achievement.event.AlbumCreatedEvent;
import faang.school.achievement.handler.AbstractAchievementHandler;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementService;
import org.springframework.stereotype.Service;

@Service
public class LibrarianAchievementHandler extends AbstractAchievementHandler<AlbumCreatedEvent> {

    private final static String ACHIEVEMENT_TITLE = "LIBRARIAN";

    public LibrarianAchievementHandler(AchievementCache achievementCache,
                                       AchievementService achievementService) {
        super(achievementService, achievementCache);
    }

    @Override
    public void handle(AlbumCreatedEvent event) {
        handleAchievement(event.getUserId(), ACHIEVEMENT_TITLE);
    }
}
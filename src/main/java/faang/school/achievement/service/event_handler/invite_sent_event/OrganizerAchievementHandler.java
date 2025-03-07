package faang.school.achievement.service.event_handler.invite_sent_event;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrganizerAchievementHandler extends InviteSentEventHandler {
    public OrganizerAchievementHandler(AchievementCache achievementCache,
                                       AchievementService achievementService,
                                       @Value("${achievements.titles.organizer}") String achievementTitle) {
        super(achievementCache, achievementService, achievementTitle);
    }
}

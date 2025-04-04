package faang.school.achievement.event_handler.comment_sent_event;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.service.AchievementService;

public class ExpertAchievementHandler extends CommentSentEventHandler {
    public ExpertAchievementHandler(AchievementCache achievementCache,
                                    AchievementService achievementService,
                                    String achievementTitle) {
        super(achievementCache, achievementService, achievementTitle);
    }
}

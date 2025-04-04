package faang.school.achievement.event_handler.comment_sent_event;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.event.CommentEvent;
import faang.school.achievement.event_handler.AbstractEventHandler;
import faang.school.achievement.service.AchievementService;

public class CommentSentEventHandler extends AbstractEventHandler<CommentEvent> {
    public CommentSentEventHandler(AchievementCache achievementCache,
                                   AchievementService achievementService,
                                   String achievementTitle) {
        super(achievementCache, achievementService, achievementTitle);
    }

    @Override
    public void handleEvent(CommentEvent event) {
        super.handleEvent(event);
    }

    @Override
    protected long getUserId(CommentEvent event) {
        return event.getAuthorId();
    }
}

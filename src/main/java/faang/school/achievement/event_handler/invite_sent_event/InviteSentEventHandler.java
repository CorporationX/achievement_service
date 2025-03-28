package faang.school.achievement.event_handler.invite_sent_event;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.event.InviteSentEvent;
import faang.school.achievement.event_handler.AbstractEventHandler;
import faang.school.achievement.service.AchievementService;

public abstract class InviteSentEventHandler extends AbstractEventHandler<InviteSentEvent> {

    public InviteSentEventHandler(AchievementCache achievementCache,
                                  AchievementService achievementService,
                                  String achievementTitle) {
        super(achievementCache, achievementService, achievementTitle);
    }

    @Override
    public void handleEvent(InviteSentEvent event) {
        super.handleEvent(event);
    }

    @Override
    protected long getUserId(InviteSentEvent event) {
        return event.getUserId();
    }
}

package faang.school.achievement.event_handler.project_create_event;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.event.ProjectEvent;
import faang.school.achievement.event_handler.AbstractEventHandler;
import faang.school.achievement.service.AchievementService;

public abstract class ProjectCreateEventHandler extends AbstractEventHandler<ProjectEvent> {

    public ProjectCreateEventHandler(AchievementCache achievementCache, AchievementService achievementService, String achievementTitle) {
        super(achievementCache, achievementService, achievementTitle);
    }

    @Override
    public void handleEvent(ProjectEvent event) {
        super.handleEvent(event);
    }

    @Override
    protected long getUserId(ProjectEvent event) {
        return event.getUserId();
    }
}

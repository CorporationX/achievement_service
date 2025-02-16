package faang.school.achievement.handler.manager;

import faang.school.achievement.event.TeamEvent;

public interface ManagerHandler {
    void startHandling(TeamEvent teamEvent);
}

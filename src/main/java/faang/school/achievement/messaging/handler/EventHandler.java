package faang.school.achievement.messaging.handler;

import faang.school.achievement.dto.SkillAcquiredEvent;

public interface EventHandler {

    void handleEvent(SkillAcquiredEvent event);
}

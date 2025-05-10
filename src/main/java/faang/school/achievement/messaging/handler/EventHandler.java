package faang.school.achievement.messaging.handler;

import faang.school.achievement.dto.SkillAcquiredEvent;
import jakarta.validation.Valid;

public interface EventHandler {

    void handleEvent(@Valid SkillAcquiredEvent event);
}

package faang.school.achievement.handler.team;

import faang.school.achievement.dto.TeamEvent;
import faang.school.achievement.handler.AbstractEventHandler;
import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class EmpireAchievementHandler implements EventHandler<TeamEvent> {

    private static final String TITLE_ACHIEVEMENT = "EMPIRE";

    private final AbstractEventHandler eventHandler;

    @Async("teamHandleAsync")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleEvent(TeamEvent event) {
        eventHandler.processAchievement(event, TITLE_ACHIEVEMENT, event.creatorId());
    }
}

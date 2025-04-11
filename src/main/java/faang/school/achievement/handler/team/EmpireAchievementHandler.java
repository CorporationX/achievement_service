package faang.school.achievement.handler.team;

import faang.school.achievement.AchievementService;
import faang.school.achievement.dto.TeamEvent;
import faang.school.achievement.handler.EventHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EmpireAchievementHandler extends TeamEventHandler implements EventHandler<TeamEvent> {

    private static final String TITLE_ACHIEVEMENT = "EMPIRE";

    public EmpireAchievementHandler(AchievementService achievementService) {
        super(achievementService);
    }

    @Override
    @Async("teamHandleAsync")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleEvent(TeamEvent event) {
        processAchievement(event, TITLE_ACHIEVEMENT);
    }
}

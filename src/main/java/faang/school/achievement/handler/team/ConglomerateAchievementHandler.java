package faang.school.achievement.handler.team;

import faang.school.achievement.service.AchievementService;
import faang.school.achievement.dto.TeamEvent;
import faang.school.achievement.handler.EventHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ConglomerateAchievementHandler extends TeamEventHandler implements EventHandler<TeamEvent> {

    private static final String TITLE_ACHIEVEMENT = "CONGLOMERATE";

    public ConglomerateAchievementHandler(AchievementService achievementService) {
        super(achievementService);
    }

    @Override
    @Async("teamHandleAsync")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleEvent(TeamEvent event) {
        processAchievement(event, TITLE_ACHIEVEMENT);
    }
}

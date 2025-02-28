package faang.school.achievement.handler.manager;

import faang.school.achievement.event.TeamEvent;
import faang.school.achievement.service.achievement.ManagerAchievementTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class ManagerAchievementHandler implements ManagerHandler {

    private final ManagerAchievementTransactionService transactionService;

    @Override
    @Async
    public void startHandling(TeamEvent teamEvent) {
        transactionService.processWithTransaction(teamEvent);
    }
}


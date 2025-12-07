package faang.school.achievement.handlers;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.CommentEventDto;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.TransactionalLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExpertAchievementHandler extends AbstractEventHandler<CommentEventDto> {
    private static final long HANDLER_EXECUTION_TIME_MS = 5000;

    public ExpertAchievementHandler(AchievementService achievementService,
                                    @Value("EXPERT") String achievementName,
                                    AchievementCache achievementCache,
                                    TransactionalLockService lockService) {
        super(achievementService, achievementName, achievementCache, lockService);
    }

    @Override
    public long getHandlerExecutionTime() {
        return HANDLER_EXECUTION_TIME_MS;
    }
}
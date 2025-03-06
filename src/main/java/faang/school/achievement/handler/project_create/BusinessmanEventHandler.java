package faang.school.achievement.handler.project_create;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.event.ProjectCreateEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class BusinessmanEventHandler implements ProjectCreateEventHandler {
    private final AchievementCache achievementCache;
    private final AchievementService achievementService;

    @Override
    @Async("projectCreateEventExecutor")
    @Transactional
    public CompletableFuture<Void> handle(ProjectCreateEvent event) {
        try {
            Achievement achievement = achievementCache.get("BUSINESSMAN");
            long userId = event.getAuthorId();

            achievementService.processAchievement(achievement, userId);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}

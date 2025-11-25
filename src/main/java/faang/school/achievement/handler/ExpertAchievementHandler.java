package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.event.CommentEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExpertAchievementHandler implements EventHandler<CommentEvent> {
    @Value("${achievement.expert.title}")
    private String achievementTitle;
    @Value("${achievement.expert.required-comments}")
    private int requiredComments;

    private final AchievementCache achievementCache;
    private final AchievementService achievementService;

    @Async
    @Override
    @Transactional
    public CompletableFuture<Void> handle(CommentEvent commentEvent) {
        try {

            log.info("Processing a user's comment {}: {}", commentEvent.getAuthorId(), commentEvent);

            long userId = commentEvent.getAuthorId();
            Optional<Achievement> achievementOpt = achievementCache.get(achievementTitle);
            if (achievementOpt.isEmpty()) {
                log.warn("❌ Achievement {} not found in cache", achievementTitle);
                return CompletableFuture.completedFuture(null);
            }

            Achievement achievement = achievementOpt.get();

            achievementService.createProgressIfNecessary(userId, achievement.getId());
            AchievementProgress progress = achievementService.getProgress(userId, achievement.getId());
            progress.increment();
            achievementService.updateProgress(progress);

            log.info("✅ User {} progress updated: {} comments (required: {})",
                    userId, progress.getCurrentPoints(), requiredComments);

            if (!achievementService.hasAchievement(userId, achievement.getId())
                    && progress.getCurrentPoints() >= requiredComments) {
                achievementService.giveAchievement(userId, achievement.getId());
                log.info("✅ Achievement '{}' granted to user {}", achievementTitle, userId);
            }

            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}

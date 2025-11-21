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

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExpertAchievementHandler implements EventHandler {
    @Value("${achievement.expert.title}")
    private String achievementTitle;
    @Value("${achievement.expert.required-comments}")
    private int requiredComments;

    private final AchievementCache achievementCache;
    private final AchievementService achievementService;

    @Async
    @Override
    public void handle(Object event) {
        if (!(event instanceof CommentEvent commentEvent)) return;

        log.info("Processing a user's comment {}: {}", commentEvent.getAuthorId(), commentEvent);

        long userId = commentEvent.getAuthorId();
        Optional<Achievement> achievementOpt = achievementCache.get(achievementTitle);
        if (achievementOpt.isEmpty()) {
            log.warn("❌ Achievement {} not found in cache", achievementTitle);
            return;
        }

        Achievement achievement = achievementOpt.get();
        if (achievementService.hasAchievement(userId, achievement.getId())) {
            log.debug(" \uD83D\uDFE2 User {} already has an achievement {}", userId, achievement.getId());
            return;
        }

        achievementService.createProgressIfNecessary(userId, achievement.getId());
        AchievementProgress progress = achievementService.getProgress(userId, achievement.getId());
        progress.increment();
        achievementService.updateProgress(progress);

        log.info("✅ User {} progress updated: {} comments (required: {})",
                userId, progress.getCurrentPoints(), requiredComments);

        if (progress.getCurrentPoints() >= requiredComments) {
            achievementService.giveAchievement(userId, achievement.getId());
            log.info("✅ Achievement '{}' granted to user {}", achievementTitle, userId);
        }
    }

}

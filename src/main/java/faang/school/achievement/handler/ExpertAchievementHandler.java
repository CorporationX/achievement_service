package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.event.CommentEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Optional;

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

        long userId = commentEvent.getAuthorId();
        Optional<Achievement> achievementOpt = achievementCache.get(achievementTitle);
        if (achievementOpt.isEmpty()) {
            return;
        }

        Achievement achievement = achievementOpt.get();
        if (achievementService.hasAchievement(userId, achievement.getId())) {
            return;
        }

        achievementService.createProgressIfNecessary(userId, achievement.getId());
        AchievementProgress progress = achievementService.getProgress(userId, achievement.getId());
        progress.increment();
        achievementService.updateProgress(progress);

        if (progress.getCurrentPoints() >= requiredComments) {
            achievementService.giveAchievement(userId, achievement.getId());
        }
    }

}

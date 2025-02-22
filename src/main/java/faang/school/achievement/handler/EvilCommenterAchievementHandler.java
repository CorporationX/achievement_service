package faang.school.achievement.handler;

import faang.school.achievement.dto.event.CommentEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EvilCommenterAchievementHandler implements AchievementHandler<CommentEvent> {

    private static final String ACHIEVEMENT_TITLE = "EVIL COMMENTER";

    private final AchievementRepository achievementRepository;
    private final AchievementService achievementService;

    @Async("fixedExecutorService")
    @Override
    public void applyAchievement(CommentEvent event) {
        Achievement achievement = getAchievementByTitle(ACHIEVEMENT_TITLE);
        boolean hasAchievement = achievementService.hasAchievement(event.authorId(), achievement.getId());
        if (!hasAchievement) {
            achievementService.createProgressIfNecessary(event.authorId(), achievement.getId());
            AchievementProgress progress = achievementService.getProgress(event.authorId(), achievement.getId());
            progress.setCurrentPoints(progress.getCurrentPoints() + 1);
            achievementService.saveProgress(progress);
            if (progress.getCurrentPoints() >= achievement.getPoints()) {
                achievementService.saveUserAchievement(achievement, event.authorId());
            }
        }
    }

    @Cacheable(key = "#title", value = "getAchievementByTitle")
    private Achievement getAchievementByTitle(String title) {
        return achievementRepository.findByTitle(title);
    }
}

package faang.school.achievement.service;

import faang.school.achievement.dto.PostEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
public class AbstractAchievementHandler implements EventHandler<PostEvent> {
    protected final AchievementService achievementService;
    private final String achievementTitle;

    @Async
    @Override
    public void handle(PostEvent event) {
        Achievement writerAchievement = achievementService.getAchievementByTitle(achievementTitle);

        if (writerAchievement == null) {
            return;
        }

        Long userId = event.getAuthorId();
        if (achievementService.hasAchievement(userId, writerAchievement.getId())) {
            return;
        }

        AchievementProgress achievementProgress =
                achievementService.getOrCreateProgress(userId, writerAchievement.getId());

        achievementProgress.increment();

        achievementService.updateProgress(achievementProgress);

        if (achievementProgress.getCurrentPoints() >= writerAchievement.getPoints()) {
            achievementService.giveAchievement(userId, writerAchievement.getId());
        }
    }
}

package faang.school.achievement.dto.handler2;

import faang.school.achievement.dto.PostEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OpinionLeaderAchievementHandler extends EventHandler<PostEvent> {
    private static final String ACHIEVEMENT_NAME = "OPINION_LEADER";

    private final AchievementService achievementService;

    private final CacheService<Achievement> cacheService;

    @Transactional
    @Override
    @Async
    public void handle(PostEvent event) {
        Achievement achievement = cacheService.get(ACHIEVEMENT_NAME, Achievement.class);

        if (!achievementService.hasAchievement(event.getAuthorId(), achievement.getId())) {
            achievementService.createProgressIfNecessary(event.getAuthorId(), achievement.getId());

            AchievementProgress achievementProgress = achievementService.getProgress(event.getAuthorId(), achievement.getId());
            achievementProgress.increment();

            if (achievementProgress.getCurrentPoints() >= achievement.getPoints()) {
                achievementService.giveAchievement(event.getAuthorId(), achievement);
            }
        }
    }
}

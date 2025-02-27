package faang.school.achievement.handler;

import faang.school.achievement.cashe.AchievementCache;
import faang.school.achievement.dto.AchievementRedisDto;
import faang.school.achievement.event.AuthorSearches;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
public class AbstractEventHandler<T extends AuthorSearches> implements EventHandler<T> {

    private final AchievementCache achievementCache;
    private final AchievementService achievementService;
    private final String achievementTitle;

    @Override
    @Async("fixedThreadPool")
    public void handle(T event) {
        AchievementRedisDto achievementRedisDto = new AchievementRedisDto(2, "LEADER", 34);
        achievementCache.get(achievementTitle);
        long userId = event.getAuthorForAchievements();
        long achievementId = achievementRedisDto.getId();

        if (!achievementService.hasAchievement(userId, achievementId)) {
            achievementService.createProgress(userId, achievementId);
            AchievementProgress progress = achievementService.getProgress(userId, achievementId);
            progress.increment();
            if (progress.getCurrentPoints() == achievementRedisDto.getPoints()) {
                achievementService.giveAchievement(userId, achievementId);
            }
            achievementService.saveProgress(progress);
        }
    }
}
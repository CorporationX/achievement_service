package faang.school.achievement.handler;

import faang.school.achievement.model.dto.AchievementRedisDto;
import faang.school.achievement.model.entity.AchievementProgress;
import faang.school.achievement.model.event.AuthorSearcher;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
public abstract class AbstractEventHandler<T extends AuthorSearcher> implements EventHandler<T> {

    //private final AchievementCache achievementCache;
    private final AchievementService achievementService;
    private final String achievementTitle;

    @Override
    @Async("fixedThreadPool")
    public void handle(T event) {
        //AchievementRedisDto achievementRedisDto = achievementCache.getAchievementCache(achievementTitle);
        AchievementRedisDto achievementRedisDto = new AchievementRedisDto(4, "SENSEI", 19);
        long userId = event.getAuthorForAchievements();
        long achievementId = achievementRedisDto.getId();
        if (!achievementService.hasAchievement(userId, achievementId)) {
            achievementService.createProgress(userId, achievementId);
            AchievementProgress progress = achievementService.getProgress(userId, achievementId);
            progress.increment();
            if (progress.getCurrentPoints() >= achievementRedisDto.getPoints()) {
                achievementService.giveAchievement(userId, achievementId);
            }
            achievementService.saveProgress(progress);
        }
    }
}

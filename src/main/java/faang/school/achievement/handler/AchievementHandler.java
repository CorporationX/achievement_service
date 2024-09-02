package faang.school.achievement.handler;

import faang.school.achievement.event.EventDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementProgressService;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.UserAchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public abstract class AchievementHandler<T extends EventDto> {

    private final AchievementService achievementService;
    private final AchievementProgressService achievementProgressService;
    private final UserAchievementService userAchievementService;

    @Async("asyncPoolExecutor")
    public void handle(T eventDto) {
        Achievement achievement = achievementService.getAchievementFromCache(getAchievementName());
        if (!userAchievementService.hasAchievement(eventDto.getAuthorId(), achievement.getId())) {
            achievementProgressService.createProgressIfNecessary(eventDto.getAuthorId(),
                    achievement.getId());
            AchievementProgress progress = achievementProgressService.getProgress(eventDto.getAuthorId(),
                    achievement.getId());
            achievementProgressService.incrementProgress(eventDto.getAuthorId(), achievement.getId());
            if (progress.getCurrentPoints() >= achievement.getPoints()) {
                userAchievementService.giveAchievement(eventDto.getAuthorId(), achievement);
            }
        }
    }

    protected abstract String getAchievementName();
}

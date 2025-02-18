package faang.school.achievement.handler;

import faang.school.achievement.dto.album.AlbumCreatedEvent;
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
public class LibrarianAchievementHandler implements AchievementHandler {

    private final AchievementRepository achievementRepository;
    private final AchievementService achievementService;

    @Async("fixedExecutorService")
    public void applyAchievement(AlbumCreatedEvent event) {
        Achievement achievement = getAchievementByTitle(String.valueOf(event.getTitle()));
        boolean hasAchievement = achievementService.hasAchievement(event.getUserId(), achievement.getId());
        if (!hasAchievement) {
            achievementService.createProgressIfNecessary(event.getUserId(), achievement.getId());
            AchievementProgress progress = achievementService.getProgress(event.getUserId(), achievement.getId());
            progress.setCurrentPoints(progress.getCurrentPoints() + 1);
            achievementService.saveProgress(progress);
            if (progress.getCurrentPoints() == achievement.getPoints()) {
                achievementService.saveUserAchievement(achievement, event.getUserId());
            }
        }
    }

    @Cacheable(key = "#title", value = "getAchievementByTitle")
    private Achievement getAchievementByTitle(String title) {
        return achievementRepository.findByTitle(title);
    }

}

package faang.school.achievement.handler;

import faang.school.achievement.dto.ProjectEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.achievement.AchievementService;
import faang.school.achievement.service.cache.AchievementCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class BusinessmanAchievementHandler implements AchievementHandler<ProjectEvent> {
    private final AchievementCacheService achievementCacheService;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementService achievementService;

    @Override
    @Async("taskExecutor")
    public void handleAchievement(ProjectEvent event) {
        log.info("Handling achievement for event: {}", event);
        Achievement businessman = achievementCacheService.getAchievementByTitle("Businessman");
        if (!hasAchievement(event, businessman)) {
            achievementProgressRepository.createProgressIfNecessary(event.getAuthorId(), businessman.getId());
        }
        if (!hasAchievement(event, businessman)) {
            AchievementProgress achievementProgress = achievementService.getProgress(event.getAuthorId(), businessman.getId());
            achievementProgress.increment();
            achievementProgressRepository.save(achievementProgress);
            if (achievementProgress.getCurrentPoints() >= businessman.getPoints()) {
                achievementService.giveAchievement(event.getAuthorId(), businessman);
            }
        }
    }

    @Override
    public boolean hasAchievement(ProjectEvent event, Achievement businessman) {
        return userAchievementRepository.existsByUserIdAndAchievementId(event.getAuthorId(), businessman.getId());
    }
}

package faang.school.achievement.handler;

import faang.school.achievement.config.cache.AchievementCache;
import faang.school.achievement.dto.event.MentorshipStartEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class SenseyAchievementHandler implements EventHandler<MentorshipStartEvent> {

    private static final String ACHIEVEMENT_NAME = "SENSEI";
    private final AchievementCache achievementCache;
    private final AchievementService achievementService;

    @Async("executor")
    @Transactional
    @Override
    public void handleEvent(MentorshipStartEvent event) {
        log.info("Thread name - {}", Thread.currentThread().getName());
        Achievement achievement = achievementCache.getAchievement(ACHIEVEMENT_NAME);
        boolean hasAchievement = achievementService
                .hasAchievement(event.getMentorId(), achievement.getId());

        if (!hasAchievement) {
            achievementService.createProgressIfNecessary(event.getMentorId(), achievement.getId());

            AchievementProgress progress = achievementService.getProgress(event.getMentorId(), achievement.getId());
            progress.increment();

            if (progress.getCurrentPoints() >= achievement.getPoints()) {
                achievementService.giveAchievement(event.getMentorId(), achievement);
            }
        }
    }
}

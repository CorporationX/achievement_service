package faang.school.achievement.scheduler;

import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class AchievementChecker {
    private final AchievementService achievementService;

    @Scheduled(cron = "${achievement.check-progresses.scheduler.cron}")
    public void checkAchievementProgresses() {
        List<AchievementProgress> progresses = achievementService.getAllNotCompletedProgresses();
        progresses.forEach(ap -> {
            if (ap.getCurrentPoints() >= ap.getAchievement().getThreshold()) {
                achievementService.assignAchievementToUser(ap);
            }
        });
    }
}

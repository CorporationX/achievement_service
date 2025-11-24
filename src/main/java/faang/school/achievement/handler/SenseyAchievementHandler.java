package faang.school.achievement.handler;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.MentorshipStartEvent;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SenseyAchievementHandler implements EventHandler<MentorshipStartEvent> {

    private final AchievementService achievementService;
    private static final String ACHIEVEMENT_TITLE = "SENSEI";

    @Async
    @Override
    @Transactional
    public void handle(MentorshipStartEvent event) {
        long mentorId = event.mentorId();
        log.info("Processing 'Sensei' achievement for user {}", mentorId);

        Achievement achievement = achievementService.getAchievementByTitle(ACHIEVEMENT_TITLE);

        if (achievementService.hasAchievement(mentorId, achievement)) {
            log.info("User {} already has achievement '{}'", mentorId, ACHIEVEMENT_TITLE);
            return;
        }

        AchievementProgress progress = achievementService.createProgressIfNecessary(mentorId, achievement);
        progress.increment();
        log.info("User {} progress for achievement '{}' incremented. Current points: {}",
                mentorId, ACHIEVEMENT_TITLE, progress.getCurrentPoints());

        if (progress.getCurrentPoints() >= achievement.getPoints()) {
            achievementService.giveAchievement(mentorId, achievement);
            log.info("User {} earned achievement '{}'", mentorId, ACHIEVEMENT_TITLE);
        }
    }
}
package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.messaging.MentorshipStartEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SenseiAchievementHandler extends EventHandler<MentorshipStartEvent> {

    public static final String ACHIEVEMENT_NAME = "SENSEI";

    public SenseiAchievementHandler(AchievementService achievementService, AchievementCache achievementCache) {
        super(achievementService, achievementCache);
    }


    @Override
    @Async("fixedThreadPool")
    public void handle(MentorshipStartEvent event) {
        Achievement achievement = achievementCache.get(ACHIEVEMENT_NAME);
        long achievementId = achievement.getId();
        long mentorId = event.mentorId();

        if (!achievementService.hasAchievement(mentorId, achievementId)) {
            achievementService.createProgressIfNecessary(mentorId, achievementId);
            AchievementProgress progress = achievementService.getProgress(mentorId, achievementId);
            progress.increment();
            if (progress.getCurrentPoints() == achievement.getPoints()) {
                achievementService.giveAchieve(mentorId, achievementId);
            }
            achievementService.saveProgress(progress);
        }


    }

}

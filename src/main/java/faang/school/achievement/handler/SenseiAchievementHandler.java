package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.messaging.MentorshipStartEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SenseiAchievementHandler implements EventHandler<MentorshipStartEvent> {

    @Value("${achievement-titles.sensei}")
    private String senseiTitle;
    private final AchievementCache achievementCache;
    private final AchievementService achievementService;


    @Override
    @Async("fixedThreadPool")
    public void handle(MentorshipStartEvent event) {
        Achievement achievement = achievementCache.get(senseiTitle);
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

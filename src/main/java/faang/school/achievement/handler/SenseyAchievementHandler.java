package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.MentorshipEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

@Component
@RequiredArgsConstructor
public class SenseyAchievementHandler {
    private final static String ACHIEVEMENT_TITLE = "SENSEI";
    private final AchievementCache cache;
    private final AchievementService service;
    private final ReentrantLock lock = new ReentrantLock();

    @Async
    public void process(MentorshipEvent event) {
        Achievement achievement = cache.get(ACHIEVEMENT_TITLE);
        Long userId = event.getMentorId();
        Long achievementId = achievement.getId();
        if (service.hasAchievement(userId, achievementId)) {
            return;
        }
        lock.lock();
        service.createProgressIfNecessary(userId, achievementId);
        Optional<AchievementProgress> progressOptional = service.getProgress(userId, achievementId);
        if (progressOptional.isEmpty()) {
            throw new RuntimeException("Что-то пошло не так");
        }
        AchievementProgress progress = progressOptional.get();
        progress.setCurrentPoints(progress.getCurrentPoints() + 1);
        service.saveProgress(progress);
        if (progress.getCurrentPoints() == achievement.getPoints()) {
            UserAchievement userAchievement = UserAchievement.builder()
                    .userId(userId)
                    .achievement(achievement)
                    .build();
            service.giveAchivement(userAchievement);
        }
        lock.unlock();
    }
}

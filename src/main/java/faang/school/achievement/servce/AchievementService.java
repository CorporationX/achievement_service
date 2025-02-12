package faang.school.achievement.servce;

import faang.school.achievement.exception.EntityNotFound;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private static final int BLOGGER_THRESHOLD = 1000;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementRepository achievementRepository;

    private Achievement getAchievement(String achievementName) {
        return achievementRepository.findByTitle(achievementName)
                .orElseThrow(() -> new EntityNotFound("Achievement not found"));
    }

    public boolean hasAchievement(Long userId, String achievementName) {
        Achievement achievement = getAchievement(achievementName);
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievement.getId());
    }

    @Transactional
    public AchievementProgress createProgressIfNecessary(Long userId, String achievementName) {
        Achievement achievement = getAchievement(achievementName);

        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievement.getId())
                .orElseGet(() -> {
                    AchievementProgress progress = AchievementProgress.builder()
                            .userId(userId)
                            .achievement(achievement)
                            .build();
                    return achievementProgressRepository.save(progress);
                });
    }

    @Transactional
    public void giveAchievement(Long userId, String achievementName) {
        Achievement achievement = getAchievement(achievementName);

        if (!userAchievementRepository.existsByUserIdAndAchievementId(userId, achievement.getId())) {
            UserAchievement userAchievement = UserAchievement.builder()
                    .userId(userId)
                    .achievement(achievement)
                    .build();
            userAchievementRepository.save(userAchievement);
        }
    }

    @Transactional
    public void incrementProgress(Long userId, String achievementTitle) {
        AchievementProgress progress = createProgressIfNecessary(userId, achievementTitle);
        progress.increment();
        achievementProgressRepository.save(progress);

        if (progress.getCurrentPoints() >= BLOGGER_THRESHOLD) {
            giveAchievement(userId, achievementTitle);
        }
    }

}

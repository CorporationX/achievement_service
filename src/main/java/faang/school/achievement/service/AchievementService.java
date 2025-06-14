package faang.school.achievement.service;

import faang.school.achievement.cash.AchievementCache;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementCache achievementCache;
    private final AchievementProgressRepository progressRepository;
    private final UserAchievementRepository userAchievementRepository;

    public boolean hasAchievement(long userId, String achievementTitle) {
        Achievement achievement = achievementCache.getByTitle(achievementTitle);
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievement.getId());
    }

    @Transactional
    public void createProgressIfNecessary(long userId, String achievementTitle) {
        Achievement achievement = achievementCache.getByTitle(achievementTitle);
        progressRepository.createProgressIfNecessary(userId, achievement.getId());
    }

    public AchievementProgress getProgress(long userId, String achievementTitle) {
        Achievement achievement = achievementCache.getByTitle(achievementTitle);
        return progressRepository.findByUserIdAndAchievementId(userId, achievement.getId())
                .orElseThrow(() -> new IllegalStateException("Progress not found"));
    }

    @Transactional
    public void incrementProgress(long userId, String achievementTitle) {
        Achievement achievement = achievementCache.getByTitle(achievementTitle);
        createProgressIfNecessary(userId, achievementTitle);
        AchievementProgress progress = getProgress(userId, achievementTitle);
        progress.increment();
        if (progress.getCurrentPoints() >= achievement.getPoints()) {
            giveAchievement(userId, achievementTitle);
        }
    }

    @Transactional
    public void giveAchievement(long userId, String achievementTitle) {
        Achievement achievement = achievementCache.getByTitle(achievementTitle);
        if (userAchievementRepository.existsByUserIdAndAchievementId(userId, achievement.getId())) {
            return;
        }
        UserAchievement userAchievement = UserAchievement.builder()
                .userId(userId)
                .achievement(achievement)
                .build();
        userAchievementRepository.save(userAchievement);
    }

    public Achievement getAchievementByTitle(String title) {
        return achievementCache.getByTitle(title);
    }
}


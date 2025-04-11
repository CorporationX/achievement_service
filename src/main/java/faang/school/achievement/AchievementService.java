package faang.school.achievement;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;

    public Optional<Achievement> getAchievementByTitle(String title) {
        return achievementRepository.findByTitle(title);
    }

    public boolean hasAchievementForUser(Long userId, Long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional
    public void createProgressIfNecessary(Long userId, Long achievementId) {
        if (getAchievementProgress(userId, achievementId).isEmpty()) {
            achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
        }
    }

    public Optional<AchievementProgress> getAchievementProgress(Long userId, Long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId);
    }

    public void giveAchievementForUser(Long userId, Achievement achievement) {
        userAchievementRepository.save(createUserAchievement(userId, achievement));
    }

    @Transactional
    public void incrementProgress(Long progressId) {
        achievementProgressRepository.increment(progressId);
    }

    private UserAchievement createUserAchievement(Long userId, Achievement achievement) {
        return UserAchievement.builder()
                .userId(userId)
                .achievement(achievement)
                .build();
    }
}

package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;

    @Transactional(readOnly = true)
    public boolean hasAchievement(long userId, Achievement achievement) {
        boolean exists = userAchievementRepository.existsByUserIdAndAchievementId(userId, achievement.getId());
        log.debug("Checked if user {} has achievement {}: {}", userId, achievement.getTitle(), exists);
        return exists;
    }

    @Transactional(readOnly = true)
    public AchievementProgress getProgress(long userId, Achievement achievement) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievement.getId())
                .orElseThrow(() -> {
                    log.error("Progress not found for user {} and achievement {}", userId, achievement.getId());
                    return new EntityNotFoundException("Progress not found for user: " + userId);
                });
    }

    @Transactional
    public AchievementProgress createProgressIfNecessary(long userId, Achievement achievement) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievement.getId());
        log.info("Ensured progress exists for user {} and achievement {}", userId, achievement.getTitle());
        return getProgress(userId, achievement);
    }

    @Transactional
    public void saveProgress(AchievementProgress progress) {
        achievementProgressRepository.save(progress);
    }

    @Transactional
    public void giveAchievement(long userId, Achievement achievement) {
        UserAchievement userAchievement = UserAchievement.builder()
                .userId(userId)
                .achievement(achievement)
                .build();

        userAchievementRepository.save(userAchievement);
        log.info("Achievement '{}' awarded to user {}", achievement.getTitle(), userId);
    }

    @Transactional(readOnly = true)
    public Achievement getAchievementByTitle(String title) {
        return achievementRepository.findByTitle(title)
                .orElseThrow(() -> {
                    log.error("Achievement with title '{}' not found in database", title);
                    return new EntityNotFoundException("Achievement not found: " + title);
                });
    }
}
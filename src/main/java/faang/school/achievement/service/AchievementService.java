package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AchievementService {
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;

    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    public void createProgressIfNecessary(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    public AchievementProgress getProgress(long userId, long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> {
                    log.info("There is no progress for achievement with id {} and user with id {}",
                            achievementId, userId);
                    return new NoSuchElementException();
                });
    }

    public UserAchievement giveAchievement(UserAchievement userAchievement) {
        return userAchievementRepository.save(userAchievement);
    }

    public void processAchievement(Achievement achievement, long userId) {
        long achievementId = achievement.getId();
        String achievementTitle = achievement.getTitle();

        log.info("Starting {} achievement handling for user: {}", achievementTitle, userId);
        if (!hasAchievement(userId, achievementId)) {
            createProgressIfNecessary(userId, achievementId);
            AchievementProgress progress = getProgress(userId, achievementId);
            progress.increment();
            log.info("{} achievement progress for user(id {}) successfully increment and now {}",
                    achievementTitle, userId, progress.getCurrentPoints());

            if (progress.getCurrentPoints() >= achievement.getPoints()) {
                UserAchievement userAchievement = UserAchievement.builder()
                        .userId(userId)
                        .achievement(achievement)
                        .build();
                UserAchievement savedUserAchievement = giveAchievement(userAchievement);
                if (savedUserAchievement != null && savedUserAchievement.getId() > 0) {
                    log.info("Gave new achievement {} for user(id {})", achievementTitle, userId);
                } else {
                    log.warn("Could not gave {} achievement for user(id {}) with unknown reason",
                            achievementTitle, userId);
                }
            }
        } else {
            log.info("{} achievement already exists for user(id {})", achievementTitle, userId);
        }
    }
}

package faang.school.achievement.service;

import faang.school.achievement.exeption.AchievementNotFoundException;
import faang.school.achievement.exeption.ProgressNotFound;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;

    public Achievement getAchievementByName(String name) {
        return achievementRepository.findByTitle(name)
                .orElseThrow(() -> {
                    log.error(AchievementNotFoundException.MESSAGE_TEMPLATE);
                    return new AchievementNotFoundException();
                });
    }

    public boolean hasAchievement(Long userId, Long achievementID) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementID);
    }

    public void createProgressIfNecessary(Long userId, Long achievementId) {
        boolean exists = achievementProgressRepository.existsByUserIdAndAchievementId(userId, achievementId);

        if (exists) return;

        Achievement achievement = getAchievementOrThrow(achievementId);

        AchievementProgress progress = AchievementProgress.builder()
                .userId(userId)
                .achievement(achievement)
                .currentPoints(0)
                .build();

        achievementProgressRepository.save(progress);
    }

    public AchievementProgress getProgress(Long userId, Long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> {
                    log.error(ProgressNotFound.MESSAGE_TEMPLATE);
                    return new ProgressNotFound();
                });
    }

    public void saveProgress(AchievementProgress progress) {
        achievementProgressRepository.save(progress);
    }

    public void giveAchievement(Long userId, Long achievementId) {
        Achievement achievement = getAchievementOrThrow(achievementId);

        UserAchievement userAchievement = UserAchievement.builder()
                .userId(userId)
                .achievement(achievement)
                .build();

        userAchievementRepository.save(userAchievement);
    }

    private Achievement getAchievementOrThrow(Long achievementId) {
        return achievementRepository.findById(achievementId)
                .orElseThrow(() -> {
                    log.error(AchievementNotFoundException.MESSAGE_TEMPLATE);
                    return new AchievementNotFoundException();
                });
    }
}

package faang.school.achievement.service;

import faang.school.achievement.exeption.AchievementNotFoundException;
import faang.school.achievement.exeption.ProgressNotFound;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@RequiredArgsConstructor
@Validated
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

    @Transactional(readOnly = true)
    public boolean hasAchievement(@NotNull Long userId, @NotNull Long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
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

    @Transactional
    public void createAchievementProgressIfNecessary(@NotNull Long userId, @NotNull Long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    @Transactional(readOnly = true)
    public AchievementProgress getProgress(@NotNull Long userId, @NotNull Long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> {
                    log.error(ProgressNotFound.MESSAGE_TEMPLATE);
                    return new ProgressNotFound();
                });
    }

    @Transactional
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

    @Transactional
    public void giveUserAchievement(UserAchievement userAchievement) {
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

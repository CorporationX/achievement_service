package faang.school.achievement.service;

import faang.school.achievement.exception.AchievementNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AchievementServiceImpl implements AchievementService {
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementRepository achievementRepository;

    @Override
    public boolean hasAchievement(Long userId, Long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Override
    public AchievementProgress getOrCreateProgress(Long userId, Long achievementId) {
        return achievementProgressRepository
                .findByUserIdAndAchievementId(userId, achievementId)
                .orElseGet(() -> createNewProgress(userId, achievementId));
    }

    @Override
    public void giveAchievement(Long userId, Long achievementId) {
        UserAchievement userAchievement = new UserAchievement();
        userAchievement.setUserId(userId);
        userAchievement.setAchievement(achievementRepository.findById(achievementId).get());
        userAchievementRepository.save(userAchievement);
    }

    @Override
    public void updateProgress(AchievementProgress progress) {
        achievementProgressRepository.save(progress);
    }

    @Override
    public Achievement getAchievementByTitle(String title) {
        return achievementRepository.findAchievementByTitle(title).get();
    }

    private AchievementProgress createNewProgress(Long userId, Long achievementId) {
        AchievementProgress progress = new AchievementProgress();
        progress.setUserId(userId);

        Achievement achievement = achievementRepository
                .findById(achievementId)
                .orElseThrow(() -> new AchievementNotFoundException("No achievement found with id " + achievementId));

        progress.setAchievement(achievement);
        progress.setCurrentPoints(0);
        achievementProgressRepository.save(progress);
        return progress;
    }
}

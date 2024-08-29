package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final UserAchievementRepository repository;
    private final AchievementProgressRepository progressRepository;

    public boolean hasAchievement(Long userId, Long achievementId) {
        return repository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    public void createProgressIfNecessary(Long userId, Long achievementId) {
        progressRepository.createProgressIfNecessary(userId, achievementId);
    }

    public Optional<AchievementProgress> getProgress(Long userId, Long achievementId) {
        return progressRepository.findByUserIdAndAchievementId(userId, achievementId);
    }

    public void saveProgress(AchievementProgress progress) {
        progressRepository.save(progress);
    }

    public void giveAchivement(UserAchievement userAchievement) {
        repository.save(userAchievement);
    }
}

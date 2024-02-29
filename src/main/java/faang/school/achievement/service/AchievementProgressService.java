package faang.school.achievement.service;

import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AchievementProgressService {

    private final AchievementProgressRepository achievementProgressRepository;

    public AchievementProgress getProgress(long userId, long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseGet(() -> achievementProgressRepository.createProgressIfNecessary(userId, achievementId));
    }

    public void updateProgress(AchievementProgress progress) {
        achievementProgressRepository.save(progress);
    }
}
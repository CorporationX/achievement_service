package faang.school.achievement.repository.adapter;

import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementProgressRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AchievementProgressRepositoryAdapter {
    private final AchievementProgressRepository achievementProgressRepository;

    public AchievementProgress getByUserIdAndAchievementId(long userId, long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Achievement progress with user ID %d and achievement ID %d not found",
                                userId, achievementId))
                );
    }
}

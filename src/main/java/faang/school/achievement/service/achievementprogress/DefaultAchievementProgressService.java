package faang.school.achievement.service.achievementprogress;

import faang.school.achievement.exception.AchievementProgressNotFoundException;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementProgressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "app.service.achievement-progress",
        havingValue = "default",
        matchIfMissing = true
)
public class DefaultAchievementProgressService implements AchievementProgressService {

    private final AchievementProgressRepository achievementProgressRepository;

    @Transactional
    @Override
    public void createProgressIfNecessary(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    @Transactional(readOnly = true)
    @Override
    public AchievementProgress getProgress(long userId, long achievementId) {
        log.debug("Starting obtain achievement progress for user {}...", userId);
        return achievementProgressRepository
                .findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new AchievementProgressNotFoundException("Achievement progress not found"));
    }
}

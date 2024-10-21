package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.MessagePublisher;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AchievementServiceImpl implements AchievementService {
    private final AchievementRepository achievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final MessagePublisher<AchievementEvent> achievementPublisher;

    @Override
    @Cacheable(value = "achievementService::hasAchievement", key = "#userId" + '.' + "#achievementId")
    public boolean hasAchievement(Long userId, Long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Override
    public void createProgressIfNecessary(Long userId, Long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    @Override
    @Cacheable(value = "achievementService::getProgress", key = "#userId" + '.' + "#achievementId")
    public AchievementProgress getProgress(Long userId, Long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Achievement %d for user %d not found".formatted(achievementId, userId))
                );
    }

    @Override
    @CachePut(value = "achievementService::getProgress",
            key = "#achievementProgress.id" + '.' + "#achievementProgress.userId")
    public void updateProgress(AchievementProgress achievementProgress) {
        achievementProgressRepository.save(achievementProgress);
        log.info("Progress {} updated successfully", achievementProgress.getId());
    }

    @Override
    @CacheEvict(value = "achievementService::getProgress", key = "#userId" + '.' + "#achievementId")
    public void giveAchievement(Long userId, Long achievementId) {
        UserAchievement userAchievement = UserAchievement.builder()
                .userId(userId)
                .achievement(getAchievementById(achievementId))
                .build();
        userAchievementRepository.save(userAchievement);
        log.info("An achievement {} is given for user {}", achievementId, userId);

        AchievementEvent achievementEvent = AchievementEvent.builder()
                .achievementId(achievementId)
                .userId(userId)
                .createdAt(userAchievement.getCreatedAt())
                .build();
        achievementPublisher.publish(achievementEvent);
    }
    
    private Achievement getAchievementById(Long achievementId) {
        return achievementRepository.findById(achievementId)
                .orElseThrow(() -> new EntityNotFoundException("Achievement %d not found".formatted(achievementId)));
    }
}

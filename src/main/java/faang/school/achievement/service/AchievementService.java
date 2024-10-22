package faang.school.achievement.service;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.exception.ResourceNotFoundException;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final AchievementCache achievementCache;
    private final AchievementMapper achievementMapper;
    private final AchievementProgressRepository achievementProgressRepository;
    private final UserAchievementRepository userAchievementRepository;

    @Transactional(readOnly = true)
    public AchievementDto getByTitle(String title) {
        AchievementDto fromCache = achievementCache.getFromCache(title);
        if (fromCache != null) {
            return fromCache;
        }

        Achievement fromDb = findByTitle(title);
        achievementCache.addToCache(fromDb);

        return achievementMapper.toDto(fromDb);
    }

    @Transactional(readOnly = true)
    public Achievement findByTitle(String title) {
        return achievementRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement", title));
    }

    @Transactional(readOnly = true)
    public boolean isUserHasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional
    public AchievementProgress createProgressIfNecessaryAndReturn(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new ResourceNotFoundException("AchievementProgress", achievementId));
    }

    @Transactional
    public void incrementProgress(AchievementProgress achievementProgress) {
        achievementProgress.increment();
        achievementProgressRepository.save(achievementProgress);
    }

    @Transactional(readOnly = true)
    public List<AchievementProgress> getAllNotCompletedProgresses() {
        return achievementProgressRepository.findAllByCompleted(false);
    }

    @Transactional
    public void assignAchievementToUser(AchievementProgress achievementProgress) {
        achievementProgress.setCompleted(true);
        achievementProgressRepository.save(achievementProgress);

        UserAchievement userAchievement = UserAchievement.builder()
                .achievement(achievementProgress.getAchievement())
                .userId(achievementProgress.getUserId())
                .build();
        userAchievementRepository.save(userAchievement);
    }
}

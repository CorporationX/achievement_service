package faang.school.achievement.service;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
@RequiredArgsConstructor
public class AchievementService {

    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementCache achievementCache;

    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional
    public AchievementProgress getProgress(long userId, Achievement achievement) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievement.getId())
                .orElseGet(() -> saveProgressWithUserIdAndAchievement(userId, achievement));
    }


    @Transactional
    public void giveAchievement(UserAchievement userAchievement) {
        if (!userAchievementRepository.existsByUserIdAndAchievementId(userAchievement.getUserId(),
                userAchievement.getAchievement().getId())) {
            userAchievementRepository.save(userAchievement);
        }
    }

    public Achievement getAchievementByName(String name) {
        Optional<Achievement> achievement = achievementCache.getAchievement(name);
        if (achievement.isPresent()) {
            return achievement.get();
        } else {
            throw new EntityNotFoundException("No achievement with name " + name);
        }
    }

    public void incrementAchievementProgress(AchievementProgress achievementProgress) {
        AtomicLong currentPoints = new AtomicLong(achievementProgress.getCurrentPoints());
        currentPoints.incrementAndGet();
        achievementProgress.setCurrentPoints(currentPoints.get());
    }

    public void updateAchievementProgress(AchievementProgress achievementProgress) {
        achievementProgressRepository.save(achievementProgress);
    }

    private AchievementProgress saveProgressWithUserIdAndAchievement(long userId, Achievement achievement) {
        return achievementProgressRepository.save(AchievementProgress.builder()
                .userId(userId)
                .achievement(achievement)
                .build()
        );
    }
}
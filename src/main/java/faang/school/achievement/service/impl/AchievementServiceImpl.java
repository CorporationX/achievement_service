package faang.school.achievement.service.impl;

import faang.school.achievement.dto.event.GiveAchievementEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.AchievementService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementServiceImpl implements AchievementService {
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public boolean hasAchievement(Long userId, Achievement achievement) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievement.getId());
    }

    @Override
    public Achievement getAchievement(String title) {
        return achievementRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Achievement not found: " + title));
    }

    @Override
    public AchievementProgress createProgressIfNecessary(Long userId, Achievement achievement) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievement.getId())
                .orElseGet(() -> {
                    log.info("Creating achievement progress for user: {} and achievement: {}", userId, achievement.getTitle());
                    return achievementProgressRepository.save(AchievementProgress.builder()
                            .achievement(achievement)
                            .userId(userId)
                            .currentPoints(0)
                            .build());
                });
    }

    @Override
    public AchievementProgress increaseAchievementProgress(AchievementProgress achievementProgress, long points) {
        log.info("Increasing achievement progress: {} by points: {}", achievementProgress.getId(), points);
        achievementProgress.setCurrentPoints(achievementProgress.getCurrentPoints() + points);
        return achievementProgressRepository.save(achievementProgress);
    }

    @Override
    public AchievementProgress getProgress(Long userId, Long achievementId) {
        return achievementProgressRepository
                .findByUserIdAndAchievementId(userId, achievementId)
                .orElse(null);
    }

    @Override
    public UserAchievement giveAchievement(Long userId, Achievement achievement) {
        log.info("Giving achievement: {} to user: {}", achievement.getTitle(), userId);

        UserAchievement userAchievement = UserAchievement.builder()
                .achievement(achievement)
                .userId(userId)
                .build();
        eventPublisher.publishEvent(new GiveAchievementEvent(this, userAchievement));
        return userAchievementRepository.save(userAchievement);
    }
}

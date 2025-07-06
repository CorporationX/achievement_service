package faang.school.achievement.service;

import faang.school.achievement.dto.ProgressCreationResultDto;
import faang.school.achievement.dto.ProgressUpdateDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.exception.AchievementNotFoundException;
import faang.school.achievement.exception.ProgressNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;

    @Cacheable(value = "achievements", key = "#title")
    public Achievement getAchievement(String title) {
        return achievementRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Achievement not found: " + title));
    }

    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional
    public ProgressCreationResultDto createProgressIfNecessary(long userId, long achievementId) {
        boolean created = achievementProgressRepository.createProgressIfNecessary(userId, achievementId) > 0;
        return ProgressCreationResultDto.builder()
                .userId(userId)
                .achievementId(achievementId)
                .created(created)
                .build();
    }

    public AchievementProgress getProgress(long userId, long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new RuntimeException("Progress not found for user " + userId +
                        " and achievement " + achievementId));
    }

    @Transactional
    public ProgressUpdateDto incrementProgress(AchievementProgress progress) {
        if (progress == null) {
            throw new ProgressNotFoundException("Progress cannot be null");
        }
        progress.setCurrentPoints(progress.getCurrentPoints() + 1);
        AchievementProgress updated = achievementProgressRepository.save(progress);
        return new ProgressUpdateDto(updated.getId(), updated.getCurrentPoints());
    }

    @Transactional
    public UserAchievementDto giveAchievement(long userId, long achievementId) {
        Optional<UserAchievement> existing = userAchievementRepository.findByUserIdAndAchievementId(userId, achievementId);
        if (existing.isPresent()) {
            return toDto(existing.get());
        }

        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new AchievementNotFoundException("Achievement not found: " + achievementId));

        UserAchievement userAchievement = UserAchievement.builder()
                .userId(userId)
                .achievement(achievement)
                .build();

        return toDto(userAchievementRepository.save(userAchievement));
    }

    private UserAchievementDto toDto(UserAchievement userAchievement) {
        return UserAchievementDto.builder()
                .id(userAchievement.getId())
                .userId(userAchievement.getUserId())
                .achievementId(userAchievement.getAchievement().getId())
                .achievedAt(userAchievement.getCreatedAt())
                .build();
    }
}
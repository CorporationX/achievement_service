package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.event.achievement.AchievementEvent;
import faang.school.achievement.mapper.achievement.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.AchievementPublisher;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementCache achievementCache;
    private final AchievementMapper achievementMapper;
    private final AchievementProgressRepository achievementProgressRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementPublisher achievementPublisher;

    public AchievementDto get(String title) {
        log.info("Requested achievement with title " + title);
        return achievementMapper.toDto(achievementCache.get(title));
    }

    public List<AchievementDto> getAll() {
        log.info("Requested all achievements");
        return achievementMapper.toDtoList(achievementCache.getAll());
    }

    @Transactional(readOnly = true)
    public boolean hasAchievement(long userId, long achievementId) {
        log.info("Checking the presence of the achievement with ID={} for a user with ID={}", achievementId, userId);
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional
    public void createProgressIfNecessary(long userId, long achievementId) {
        log.info("Creating progress record for achievement with ID={} if necessary for user with ID={}", achievementId, userId);
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    @Transactional(readOnly = true)
    public AchievementProgress getProgress(long userId, long achievementId) {
        log.info("Getting progress for achievement with ID={} for user with ID={}", achievementId, userId);
        AchievementProgress progress = achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> {
                    String errorMessage = "Progress for achievement ID=%d and user ID=%d not found".formatted(achievementId, userId);
                    log.warn(errorMessage);
                    return new EntityNotFoundException(errorMessage);
                });
        return progress;
    }

    @Transactional
    public void giveAchievement(long userId, Achievement achievement) {
        log.info("Giving achievement titled '{}' to user with ID={}", achievement.getTitle(), userId);

        UserAchievement userAchievement = buildUserAchievement(userId, achievement);
        userAchievementRepository.save(userAchievement);
        log.info("Achievement '{}' saved for user ID={}", achievement.getTitle(), userId);

        achievementPublisher.publish(buildAchievementEvent(userAchievement));
        log.info("AchievementEvent published for user ID={} and achievement '{}'", userId, achievement.getTitle());
    }

    @Transactional
    public void updateProgress(AchievementProgress achievementProgress) {
        achievementProgressRepository.save(achievementProgress);
        log.info("Progress with ID={} was updated successfully", achievementProgress.getId());
    }

    private UserAchievement buildUserAchievement(long userId, Achievement achievement) {
        return UserAchievement.builder()
                .achievement(achievement)
                .userId(userId)
                .build();
    }

    private AchievementEvent buildAchievementEvent(UserAchievement userAchievement) {
        Achievement achievement = userAchievement.getAchievement();
        return AchievementEvent.builder()
                .userId(userAchievement.getUserId())
                .title(achievement.getTitle())
                .description(achievement.getDescription())
                .build();
    }
}

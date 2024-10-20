package faang.school.achievement.service.achievement;

import faang.school.achievement.dto.achievement.AchievementProgressDto;
import faang.school.achievement.mapper.achievement.AchievementMapper;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.achievement.UserAchievementPublisher;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementService {

    private final AchievementProgressRepository achievementProgressRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementMapper achievementMapper;
    private final UserAchievementPublisher userAchievementPublisher;

    public AchievementProgressDto getAchievementProgress(long userId, long achievementId) {
        log.debug("Returning progress user {} progress on {}", userId, achievementId);
        return achievementMapper.toAchievementProgressDto(getProgress(userId, achievementId));
    }

    public boolean hasAchievement(long userId, long achievementId) {
        log.debug("Checking whether user {} has achievement {}", userId, achievementId);
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional
    public void createProgressIfNecessary(long userId, long achievementId) {
        log.debug("Checking whether user with id {} has progress on {}", userId, achievementId);
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    public AchievementProgress getProgress(long userId, long achievementId) {
        log.debug("Getting user {} progress on achievement {}", userId, achievementId);
        return achievementProgressRepository
                .findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new EntityNotFoundException("There are no achievement progress with id " +
                        achievementId + " for user " + userId + " found!"));
    }

    @Transactional
    public void giveAchievement(UserAchievement achievement) {
        log.info("User {} has achieved {}!", achievement.getUserId(), achievement.getAchievement().getTitle());
        userAchievementRepository.save(achievement);
        log.debug("Successfully saved new achievement {} for user {}", achievement.getAchievement().getTitle(),
                achievement.getUserId());
        userAchievementPublisher.publish(achievementMapper.toUserAchievementDto(achievement));
        log.debug("Published achievement {} to {}", achievement.getAchievement().getTitle(),
                userAchievementPublisher.getAchievementChannelTopic());
    }

    @Transactional
    public AchievementProgress saveAchievementProgress(AchievementProgress achievementProgress) {
        log.debug("Saving achievement progress... {}", achievementProgress.getAchievement().getTitle());
        return achievementProgressRepository.save(achievementProgress);
    }

    @Transactional
    public AchievementProgress proceedAchievementProgress(long userId, long achievementId) {
        createProgressIfNecessary(userId, achievementId);
        AchievementProgress progress = getProgress(userId, achievementId);
        long progressPoints = progress.getCurrentPoints();
        progress.setCurrentPoints(progressPoints + 1);
        return saveAchievementProgress(progress);
    }
}

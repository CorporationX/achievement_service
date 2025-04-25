package faang.school.achievement.service;

import faang.school.achievement.message.ErrorMessage;
import faang.school.achievement.exception.AchievementNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AchievementServiceImpl implements AchievementService {
    private static final String ACHIEVEMENT_NOT_FOUND_MSG = "Achievement not found with id: %d";
    private static final String ACHIEVEMENT_NOT_FOUND_LOG = "Achievement not found with id: {}";

    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementRepository achievementRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean hasAchievement(Long userId, Long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Override
    @Transactional
    public void createProgressIfNecessary(Long userId, Long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    @Override
    public AchievementProgress getOrCreateProgress(Long userId, Long achievementId) {
        return achievementProgressRepository
                .findByUserIdAndAchievementId(userId, achievementId)
                .orElseGet(() -> createNewProgress(userId, achievementId));
    }

    @Override
    @Transactional(readOnly = true)
    public AchievementProgress getProgress(Long userId, Long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() ->
                        new IllegalArgumentException(String.format(ErrorMessage.
                                ACHIEVEMENT_PROGRESS_NOT_FOUND_BY_ID_AND_ACHIEVEMENT_ID.format(userId, achievementId))));
    }

    @Override
    @Transactional
    public void giveAchievement(AchievementProgress achievementProgress) {
        long userId = achievementProgress.getUserId();
        long achievementId = achievementProgress.getAchievement().getId();

        if (!userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)) {
            UserAchievement userAchievement = new UserAchievement();
            userAchievement.setUserId(userId);
            userAchievement.setAchievement(achievementProgress.getAchievement());
            userAchievement.setCreatedAt(LocalDateTime.now());
            userAchievementRepository.save(userAchievement);
        }
    }

    @Override
    public void giveAchievement(Long userId, Long achievementId) {
        log.info("Getting or creating progress for user {} and achievement {}",
                userId, achievementId);
        if (hasAchievement(userId, achievementId)) {
            log.warn("User {} already has achievement {}", userId, achievementId);
            return;
        }
        UserAchievement userAchievement = new UserAchievement();
        userAchievement.setUserId(userId);
        userAchievement.setAchievement(getAchievementFromRepoById(achievementId));
        userAchievementRepository.save(userAchievement);
        log.info("Achievement {} granted to user {}", achievementId, userId);
    }

    @Override
    public void updateProgress(AchievementProgress progress) {
        log.info("Updating progress for user {} and achievement {}",
                progress.getUserId(), progress.getAchievement().getId());
        achievementProgressRepository.save(progress);
    }

    @Override
    @Transactional(readOnly = true)
    public Achievement getAchievementFindByTitle(String title) {
        return achievementRepository.findByTitle(title).orElseThrow(() ->
                new IllegalArgumentException(String.format(ErrorMessage.ACHIEVEMENT_NOT_FOUND_BY_TITLE.format(title))));
    }

    @Override
    @Transactional
    public long incrementProgress(AchievementProgress progress) {
        achievementProgressRepository.incrementPoints(progress.getId());
        AchievementProgress updated = achievementProgressRepository.findById(progress.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException(ErrorMessage.ACHIEVEMENT_PROGRESS_NOT_FOUND_BY_ID.format(progress.getId())));
        return updated.getCurrentPoints();
    }

    @Override
    public Achievement getAchievementByTitle(String title) {
        return achievementRepository.findAchievementByTitle(title)
                .orElseThrow(() -> {
                    log.error("Achievement not found with title: {}", title);
                    return new AchievementNotFoundException("No achievement found with title " + title);
                });
    }

    private AchievementProgress createNewProgress(Long userId, Long achievementId) {
        log.info("Creating new progress for user {} and achievement {}",
                userId, achievementId);
        AchievementProgress progress = new AchievementProgress();
        progress.setUserId(userId);

        Achievement achievement = getAchievementFromRepoById(achievementId);

        progress.setAchievement(achievement);
        progress.setCurrentPoints(0);
        achievementProgressRepository.save(progress);
        return progress;
    }

    private Achievement getAchievementFromRepoById(Long achievementId) {
        return achievementRepository
                .findById(achievementId)
                .orElseThrow(() -> {
                    log.error(ACHIEVEMENT_NOT_FOUND_LOG, achievementId);
                    return new AchievementNotFoundException(
                            String.format(ACHIEVEMENT_NOT_FOUND_MSG, achievementId)
                    );
                });
    }
}

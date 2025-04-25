package faang.school.achievement.service;

import faang.school.achievement.message.ErrorMessage;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AchievementServiceImpl implements AchievementService {
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

        if(!userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId))
        {
            UserAchievement userAchievement = new UserAchievement();
            userAchievement.setUserId(userId);
            userAchievement.setAchievement(achievementProgress.getAchievement());
            userAchievement.setCreatedAt(LocalDateTime.now());
            userAchievementRepository.save(userAchievement);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Achievement getAchievementByTitle(String title) {
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
}

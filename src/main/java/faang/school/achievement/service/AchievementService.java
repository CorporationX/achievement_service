package faang.school.achievement.service;

import faang.school.achievement.cashe.AchievementCache;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementService {

    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository progressRepository;
    private final AchievementCache achievementCache;

    @Transactional(readOnly = true)
    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional
    public void createProgress(long userId, long achievementId) {
        progressRepository.createProgressIfNecessary(userId, achievementId);
    }

    @Transactional(readOnly = true)
    public AchievementProgress getProgress(long userId, long achievementId) {
        return progressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new EntityNotFoundException
                        ("Достижение с id %s не существует для пользователя с id %s "
                                .formatted(achievementId, userId)));
    }

    @Transactional
    public void giveAchievement(long userId, String achievementTitle) {
        Achievement achievement = achievementCache.get(achievementTitle);

        UserAchievement userAchievement = UserAchievement.builder()
                .achievement(achievement)
                .userId(userId)
                .build();

        userAchievementRepository.save(userAchievement);
    }

    public void saveProgress(AchievementProgress progress) {
        progressRepository.save(progress);
    }
}
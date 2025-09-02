package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.mapper.AchievementMapper;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementMapper mapper;

    public AchievementDto findByTitle(String title) {
        Achievement achievement = achievementRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Achievement not found by title={}", title));
        return mapper.toAchievementDto(achievement);
    }

    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    public void createProgressIfNecessary(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    public AchievementProgress getProgress(long userId, long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "AchievementProgress not found by userId={} и achievementId={}",
                        userId, achievementId));
    }

    public void giveAchievement(long achievementId, long userId) {
        UserAchievement userAchievement = new UserAchievement();
        Achievement achievement = Achievement.builder()
                .id(achievementId)
                .build();
        userAchievement.setAchievement(achievement);
        userAchievement.setUserId(userId);

        userAchievementRepository.save(userAchievement);
    }

    @Transactional
    public long incrementAndGetPointsById(long id) {
        achievementProgressRepository.incrementById(id);
        return achievementProgressRepository.getPointsById(id);
    }
}

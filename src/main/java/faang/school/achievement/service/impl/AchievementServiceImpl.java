package faang.school.achievement.service.impl;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.event.AchievementEvent;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.mapper.AchievementProgressMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.AchievementPublisher;
import faang.school.achievement.service.AchievementService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementServiceImpl implements AchievementService {

    private final AchievementRepository achievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementPublisher achievementPublisher;
    private final AchievementProgressMapper achievementProgressMapper;
    private final AchievementMapper achievementMapper;

    @Override
    @Cacheable(value = "user_achievement")
    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Override
    @Transactional
    public void createProgressIfNecessary(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    @Override
    @Transactional
    public void giveAchievement(long userId, long achievementId) {
        AchievementDto achievementDto = getAchievement(achievementId);
        UserAchievement userAchievement = UserAchievement.builder()
                .userId(userId)
                .achievement(achievementMapper.toAchievement(achievementDto))
                .build();
        UserAchievement savedUserAchievement = userAchievementRepository.save(userAchievement);
        log.info("User {} issued achievement {}", userId, achievementId);
        AchievementEvent achievementEvent = AchievementEvent.builder()
                .userId(userId)
                .achievementId(achievementId)
                .build();
        achievementPublisher.publishMessage(achievementEvent);
        log.info("Event {} published to achievement channel", achievementEvent);
    }

    @Cacheable(value = "achievement")
    public AchievementDto getAchievement(long achievementId) {
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new EntityNotFoundException("Not found achievement with id " + achievementId));
        return achievementMapper.toAchievementDto(achievement);
    }

    @CachePut(value = "achievement_progress")
    @Override
    @Transactional
    public AchievementProgressDto incrementProgress(long userId, long achievementId) {
        AchievementProgress achievementProgress = getProgress(userId, achievementId);
        achievementProgress.increment();
        achievementProgress = achievementProgressRepository.save(achievementProgress);
        return achievementProgressMapper.toAchievementProgressDto(achievementProgress);
    }

    @Cacheable(value = "achievement_progress")
    private AchievementProgress getProgress(long userId, long achievementId) {
        return achievementProgressRepository
                .findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new EntityNotFoundException("Not found achievement progress with id "
                        + achievementId + " of user " + userId));
    }
}

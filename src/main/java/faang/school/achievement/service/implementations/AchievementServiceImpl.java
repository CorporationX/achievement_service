package faang.school.achievement.service.implementations;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.event.AchievementEvent;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.AchievementPublisher;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.interfaces.Cache;
import faang.school.achievement.service.interfaces.AchievementService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementServiceImpl implements AchievementService {
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementRepository achievementRepository;
    private final Cache<Achievement> achievementCache;
    private final AchievementMapper achievementMapper;
    private final AchievementPublisher achievementPublisher;

    @Override
    public AchievementDto get(String title) {
        log.info("Requested achievement with title: {} ", title);
        return achievementMapper.toDto(achievementCache.get(title));
    }

    @Override
    public List<AchievementDto> getAll() {
        log.info("Requested all achievements");
        return achievementMapper.toDtoList(achievementCache.getAll());
    }

    @Transactional(readOnly = true)
    @Override
    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Transactional
    @Override
    public void createProgressIfNecessary(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    @Transactional(readOnly = true)
    @Override
    public AchievementProgress getProgress(long userId, long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new EntityNotFoundException("AchievementProgress not found"));
    }

    @Transactional
    @Override
    public void updateProgress(AchievementProgress achievementProgress) {
        achievementProgressRepository.save(achievementProgress);
        log.info("Progress with id: {} updated successfully", achievementProgress.getId());
    }

    @Transactional
    @Override
    public void giveAchievement(long userId, long achievementId) {
        Achievement achievement = getAchievementById(achievementId);
        UserAchievement userAchievement = UserAchievement.builder()
                .userId(userId)
                .achievement(achievement)
                .build();
        userAchievementRepository.save(userAchievement);
        log.info("User with id: {} received achievement with id: {}", userId, achievementId);
        AchievementEvent achievementEvent = AchievementEvent.builder()
                .title(achievement.getTitle())
                .description(achievement.getDescription())
                .userId(userId)
                .build();
        achievementPublisher.publish(achievementEvent);
        log.info("Achievement event has been published");
    }

    private Achievement getAchievementById(long achievementId) {
        return achievementRepository.findById(achievementId)
                .orElseThrow(() -> new EntityNotFoundException("Achievement not found"));
    }
}

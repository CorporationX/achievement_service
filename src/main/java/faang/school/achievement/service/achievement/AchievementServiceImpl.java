package faang.school.achievement.service.achievement;

import faang.school.achievement.dto.AchievementEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.AchievementPublisher;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.CacheService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementServiceImpl implements AchievementService {

    private final AchievementRepository achievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final UserAchievementRepository achievementUserRepository;
    private final CacheService<Achievement> cacheService;
    private final AchievementPublisher achievementPublisher;

    @PostConstruct
    public void initAchievements() {
        List<Achievement> achievements = achievementRepository.findAll();
        achievements.forEach(achievement -> cacheService.put(achievement.getTitle(), achievement));
    }

    @Override
    public boolean hasAchievement(long userId, long achievementId) {
        return achievementUserRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Override
    public AchievementProgress getProgress(long userId, long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new EntityNotFoundException("Achievement %d progress not found".formatted(achievementId)));
    }

    @Override
    public void createProgressIfNecessary(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    @Override
    public void giveAchievement(long userId, Achievement achievement) {
        UserAchievement userAchievement = UserAchievement.builder()
                .userId(userId)
                .achievement(achievement)
                .build();
        achievementUserRepository.save(userAchievement);

        AchievementEvent event = new AchievementEvent(LocalDateTime.now(), userId, achievement.getId());
        achievementPublisher.publish(event);
    }
}

package faang.school.achievement.service.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AchievementCacheServiceImpl implements AchievementCacheService {
    private final AchievementRepository achievementRepository;

    @Override
    @Cacheable(value = "achievements", key = "#id")
    public Achievement getAchievement(Long id) {
        return achievementRepository.findById(id).orElse(null);
    }
    @Override
    @Cacheable(value = "achievements", key = "#title")
    public Achievement getAchievementByTitle(String title) {
        return achievementRepository.findByTitle(title);
    }

    @Override
    public void createAchievement(Achievement achievement) {
        achievementRepository.save(achievement);
    }
}


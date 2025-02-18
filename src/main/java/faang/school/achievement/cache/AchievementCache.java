package faang.school.achievement.cache;

import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AchievementCache {

    private final AchievementRepository achievementRepository;
    private final CacheManager cacheManager;

    @PostConstruct
    private void warmUpCache() {
        List<Achievement> achievements = achievementRepository.findAll();

        Cache cache = cacheManager.getCache("achievementTitle");
        achievements.forEach(achievement -> cache.put(achievement.getTitle(), achievement));
        log.info("Кэш достижений инициализирован");
    }

    @Cacheable(value = "achievementTitle")
    public Achievement get(String title) {
        return achievementRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Достижения с названием %s не существует"
                        .formatted(title)));
    }
}

package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AchievementCache {
    public static final String ACHIEVEMENT_CACHE_NAME = "achievements";

    private final AchievementRepository achievementRepository;
    private final CacheManager cacheManager;

    @PostConstruct
    public void initCache() {
        Cache cache = cacheManager.getCache(ACHIEVEMENT_CACHE_NAME);
        if (cache == null) {
            log.error("Cache '{}' not configured!", ACHIEVEMENT_CACHE_NAME);
            throw new IllegalStateException("Cache not configured");
        }

        List<Achievement> achievements = achievementRepository.findAll();
        achievements.forEach(achievement ->
                cache.put(achievement.getTitle(), achievement)
        );
        log.info("Cache initialized with {} achievements", achievements.size());
    }

    @Cacheable(value = ACHIEVEMENT_CACHE_NAME, key = "#title")
    public Achievement get(String title) {
        log.info("Cache miss for title: {}", title);
        return achievementRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Achievement not found"));
    }

    @CachePut(value = ACHIEVEMENT_CACHE_NAME, key = "#achievement.title")
    public Achievement addOrUpdate(Achievement achievement) {
        log.info("Saving and caching achievement: {}", achievement.getTitle());
        return achievementRepository.save(achievement);
    }

    @CacheEvict(value = ACHIEVEMENT_CACHE_NAME, key = "#title")
    public void remove(String title) {
        log.info("Deleting achievement: {}", title);
        achievementRepository.deleteByTitle(title);
    }
}

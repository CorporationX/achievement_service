package faang.school.achievement.service.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementCache {
    private final AchievementRepository achievementRepository;
    private final CacheManager cacheManager;

    @PostConstruct
    public void initCache() {
        Cache cache = cacheManager.getCache("achievements");
        if (cache == null) return;

        List<Achievement> achievements = achievementRepository.findAll();
        achievements.forEach(achievement ->
                cache.put(achievement.getTitle(), achievement)
        );
        log.info("Cache initialized with {} achievements", achievements.size());
    }

    @Cacheable(value = "achievements", key = "#title")
    public Achievement get(String title) {
        log.info("Cache miss for title: {}", title);
        return achievementRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Achievement not found"));
    }

    @CachePut(value = "achievements", key = "#achievement.title")
    public Achievement addOrUpdate(Achievement achievement) {
        log.info("Saving and caching achievement: {}", achievement.getTitle());
        return achievementRepository.save(achievement);
    }

    @CacheEvict(value = "achievements", key = "#title")
    public void remove(String title) {
        log.info("Deleting achievement: {}", title);
        achievementRepository.deleteByTitle(title);
    }
}
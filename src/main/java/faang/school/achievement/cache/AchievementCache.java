package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AchievementCache {
    public static final String ACHIEVEMENT_CACHE_NAME = "achievements";

    private final AchievementRepository achievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final UserAchievementRepository userAchievementRepository;
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

    @Cacheable(value = ACHIEVEMENT_CACHE_NAME,
            key = "'filtered_' + (#title == null ? 'NULL' : #title) + '_' " +
                    "+ (#description == null ? 'NULL' : #description) + '_' " +
                    "+ (#rarity == null ? 'NULL' : #rarity)")
    public List<Achievement> getFilteredAchievements(String title,
                                                     String description,
                                                     Rarity rarity,
                                                     Pageable pageable) {
        log.info("Cache miss for achievements filtered by: {}, {}, {}, page {}, size {}",
                title, description, rarity, pageable.getPageNumber(), pageable.getPageSize());
        return achievementRepository.findFilteredAchievements(title, description, rarity, pageable);
    }

    @Cacheable(value = ACHIEVEMENT_CACHE_NAME, key = "'id_' + #id")
    public Achievement getAchievementById(Long id) {
        log.info("Cache miss for id: {}", id);
        return achievementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Achievement not found"));
    }

    @Cacheable(value = ACHIEVEMENT_CACHE_NAME, key = "'userAchievements_' + #userId")
    public List<UserAchievement> getUserAchievements(Long userId) {
        log.info("Cache miss for userAchievements, userId={}", userId);
        List<UserAchievement> achievements = userAchievementRepository.findByUserId(userId);
        if (achievements == null || achievements.isEmpty()) {
            throw new EntityNotFoundException("No achievements found for user with id: " + userId);
        }
        return achievements;
    }

    @Cacheable(value = ACHIEVEMENT_CACHE_NAME, key = "'unearnedAchievements_' + #userId")
    public List<AchievementProgress> getUserUnearnedAchievements(Long userId) {
        log.info("Cache miss for unearnedAchievements, userId={}", userId);
        List<AchievementProgress> progresses = achievementProgressRepository.findByUserId(userId);
        if (progresses == null || progresses.isEmpty()) {
            throw new EntityNotFoundException("No unearned achievements found for user with id: " + userId);
        }
        return progresses;
    }
}

package faang.school.achievement.service;


import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.entity.Achievement;
import faang.school.achievement.model.dto.AchievementRedisDto;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementCacheImpl implements AchievementCache {
    private final AchievementRepository achievementRepository;
    private final CacheManager cacheManager;
    private final AchievementMapper mapper;

    @Override
    @PostConstruct
    public void addAchievementCache() {
        List<Achievement> achievements = achievementRepository.findAll();
        achievements.forEach(achievement -> {
            AchievementRedisDto dto = mapper.toDto(achievement);
            cacheManager.getCache("achievement").put(achievement.getTitle(), dto);
        });
    }

    @Override
    @Cacheable(key = "#title", value = "achievement")
    public AchievementRedisDto getAchievementCache(String title) {
        Achievement achievement = achievementRepository.findByTitle(title).orElseThrow(() -> {
            log.error("An achievement with this name does not exist in the database. Title: {}", title);
            return new IllegalArgumentException("An achievement with this name does not exist in the database");
        });
        return mapper.toDto(achievement);
    }
}

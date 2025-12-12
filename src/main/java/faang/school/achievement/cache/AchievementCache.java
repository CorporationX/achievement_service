package faang.school.achievement.cache;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AchievementCache {
    private static final String ACHIEVEMENTS_CACHE_NAME = "ACHIEVEMENTS";
    private final AchievementRepository achievementRepository;
    private final CacheManager cacheManager;
    private final AchievementMapper achievementMapper;

    @PostConstruct
    public void fillAchievementCache() {
        log.info("Начинаем заполнять кэш при запуске приложения.");
        List<Achievement> achievements = new ArrayList<>();
        achievementRepository.findAll().forEach(achievements::add);
        Cache achievementsCache = cacheManager.getCache(ACHIEVEMENTS_CACHE_NAME);
        for (Achievement achievement : achievements) {
            AchievementDto achievementDto = achievementMapper.toAchievementDto(achievement);
            achievementsCache.put(achievementDto.title().toUpperCase(), achievementDto);
        }
        log.info("Кэш заполнен, внесено {} эллементов", achievements.size());
    }

    @Cacheable(value = ACHIEVEMENTS_CACHE_NAME, key = "#title")
    public AchievementDto getByTitle(String title) {
        Achievement achievement = achievementRepository.findByTitle(title.toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException("Такого достижения пока не существует"));
        return achievementMapper.toAchievementDto(achievement);
    }
}

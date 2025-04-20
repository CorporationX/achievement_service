package faang.school.achievement.service.implementations;

import faang.school.achievement.exception.HandleAchievementException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.service.AchievementCacheInitializer;
import faang.school.achievement.service.interfaces.AchievementRedisService;
import faang.school.achievement.service.interfaces.Cache;
import io.lettuce.core.RedisConnectionException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisAchievementCache implements Cache<Achievement> {
    private final AchievementRedisService achievementRedisService;
    private final AchievementCacheInitializer cacheInitializer;
    private final AchievementServiceImpl achievementService;

    @PostConstruct
    public void init() {
        cacheInitializer.fillCache();
    }

    @PreDestroy
    private void clearCache() {
        achievementRedisService.cleanAchievements();
        log.info("Achievement cache cleared");
    }

    @Override
    @Retryable(retryFor = {RedisConnectionException.class}, backoff = @Backoff(delay = 1000))
    public Achievement get(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new HandleAchievementException("Achievement title cannot be null or empty");
        }

        Achievement achievement = achievementRedisService.getAchievement(title);
        if (achievement != null) {
            return achievement;
        }

        if (!achievementService.existsByTitle(title)) {
            throw new HandleAchievementException("Achievement with title " + title + "' does not exist");
        }

        log.info("Achievement '{}' not found in cache, refreshing cache", title);
        clearCache();
        cacheInitializer.fillCache();

        achievement = achievementRedisService.getAchievement(title);
        if (achievement == null) {
            throw new HandleAchievementException("Achievement with title '" + title + "' not found after cache refresh");
        }

        return achievement;
    }

    @Override
    public List<Achievement> getAll() {
        return achievementRedisService.getAllAchievements();
    }
}

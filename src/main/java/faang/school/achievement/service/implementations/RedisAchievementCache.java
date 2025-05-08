package faang.school.achievement.service.implementations;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.exception.HandleAchievementException;
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
public class RedisAchievementCache implements Cache<AchievementDto> {
    private final AchievementRedisService achievementRedisService;
    private final AchievementCacheInitializer cacheInitializer;

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
    public AchievementDto get(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new HandleAchievementException("Achievement title cannot be null or empty");
        }

        AchievementDto achievementDto = achievementRedisService.getAchievement(title);
        if (achievementDto != null) {
            return achievementDto;
        }

        log.info("Achievement '{}' not found in cache, refreshing cache", title);
        clearCache();
        cacheInitializer.fillCache();

        achievementDto = achievementRedisService.getAchievement(title);
        if (achievementDto == null) {
            throw new HandleAchievementException("Achievement with title '" + title +
                    "' not found after cache refresh");
        }

        return achievementDto;
    }

    @Override
    public List<AchievementDto> getAll() {
        return achievementRedisService.getAllAchievements();
    }
}

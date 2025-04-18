package faang.school.achievement.service.implementations;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.service.AchievementCacheInitializer;
import faang.school.achievement.service.interfaces.AchievementRedisService;
import faang.school.achievement.service.interfaces.Cache;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisAchievementCache implements Cache<Achievement> {
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
    public Achievement get(String title) {
        Achievement achievement = achievementRedisService.getAchievement(title);
        if (achievement == null) {
            clearCache();
            cacheInitializer.fillCache();
            achievement = achievementRedisService.getAchievement(title);
            if (achievement == null) {
                throw new NoSuchElementException("Achievement with title " + title + " no such");
            }
        }
        return achievement;
    }

    @Override
    public List<Achievement> getAll() {
        return achievementRedisService.getAllAchievements();
    }
}

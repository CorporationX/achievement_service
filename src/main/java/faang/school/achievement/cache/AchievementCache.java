package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AchievementCache {
    private final AchievementRepository achievementRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.data.redis.cache.achievement.prefix}")
    private String achievementPrefix;

    @Value("${spring.data.redis.cache.achievement.ttl}")
    private long achievementTtlInSeconds;

    @PostConstruct
    public void initCacheUpload() {
        List<Achievement> allAchievements = achievementRepository.findAll();
        allAchievements.forEach(this::cacheAchievement);
    }

    public void cacheAchievement(Achievement achievement) {
        redisTemplate.opsForValue().set(achievementPrefix + achievement.getTitle(),
                achievement, Duration.ofSeconds(achievementTtlInSeconds));
    }

    public Optional<Achievement> get(String achievementTitle) {
        Achievement achievement = (Achievement) redisTemplate.opsForValue().get(achievementPrefix + achievementTitle);
        return Optional.ofNullable(achievement);
    }
}
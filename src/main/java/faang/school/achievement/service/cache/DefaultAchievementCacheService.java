package faang.school.achievement.service.cache;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.exception.AchievementNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "app.service.achievement-cache",
        havingValue = "default",
        matchIfMissing = true
)
public class DefaultAchievementCacheService implements AchievementCacheService {

    private final RedisTemplate<String, AchievementDto> redisCacheTemplate;
    private final AchievementKeyGenerator achievementKeyGenerator;

    @Override
    public AchievementDto getAchievement(String title) {
        String achievementKey = achievementKeyGenerator.createAchievementKey(title);

        log.debug("Starting obtain \"{}\" achievement from cache...", title);
        AchievementDto achievement = redisCacheTemplate.opsForValue().get(achievementKey);
        validateRedisResult(title, achievement);

        log.debug("Obtained successfully \"{}\" achievement from cache", title);
        return achievement;
    }

    private static void validateRedisResult(String title, AchievementDto achievement) {
        if (achievement == null) {
            log.warn("Achievement \"{}\" not found in cache", title);
            throw new AchievementNotFoundException(
                    String.format("Achievement \"%s\" not found in Redis", title)
            );
        }
    }
}

package faang.school.achievement.redis;

import faang.school.achievement.model.AchievementCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public abstract class RedisCounterService {
    private final RedisTemplate<String, Long> redisCounterTemplate;
    private final RedisTemplate<String, String> stringRedisTemplate;
    private final AchievementKeyBuilder achievementKeyBuilder;

    public RedisCounterService(@Qualifier("redisCounterTemplate") RedisTemplate<String, Long> redisCounterTemplate,
                               @Qualifier("redisTemplate") RedisTemplate<String, String> stringRedisTemplate,
                               AchievementKeyBuilder achievementKeyBuilder) {
        this.redisCounterTemplate = redisCounterTemplate;
        this.achievementKeyBuilder = achievementKeyBuilder;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public Long incrementCounter(AchievementCode achievementCode, long userId) {
        log.info("Increment counter title: {}, userId = {}", achievementCode.getName(), userId);
        String key = achievementKeyBuilder.userProgress(achievementCode.getName(), userId);
        return redisCounterTemplate.opsForValue().increment(key, 1);
    }

    public void decrementCounter(AchievementCode achievementCode, long userId, long delta) {
        log.info("Decrement counter title: {}, userId = {}", achievementCode.getName(), userId);
        String key = achievementKeyBuilder.userProgress(achievementCode.getName(), userId);
        redisCounterTemplate.opsForValue().decrement(key, delta);
    }

    public void assignAchievement(AchievementCode achievementCode, long userId) {
        log.info("Updating a cache with a completed achievement for a user, title: {}, userId = {}", achievementCode.getName(), userId);
        String key = achievementKeyBuilder.assignAchievement(userId);
        stringRedisTemplate.opsForSet().add(key, achievementCode.getName());
        removeAchievementProgressTracking(achievementCode, userId);
    }

    public void removeAchievementProgressTracking(AchievementCode achievementCode, long userId) {
        log.info("Removing tracking of the achievement to a user, title: {}, userId = {}", achievementCode.getName(), userId);
        String key = achievementKeyBuilder.userProgress(achievementCode.getName(), userId);
        stringRedisTemplate.delete(key);
    }

    public Boolean checkIfAchievementAssigned(AchievementCode achievementCode, long userId) {
        log.info("Checking if an achievement has been assigned to a user, title: {}, userId = {}",
                achievementCode.getName(), userId);
        String key = achievementKeyBuilder.assignAchievement(userId);
        Boolean isAssigned = stringRedisTemplate.opsForSet().isMember(key, achievementCode.getName());
        if (isAssigned != null) {
            log.info("Has the achievement been assigned: {}", isAssigned);
            return isAssigned;
        }
        return false;
    }
}
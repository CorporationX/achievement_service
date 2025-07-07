package faang.school.achievement.redis;

import faang.school.achievement.model.AchievementCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

@Slf4j
public abstract class RedisCounterService {
    private final RedisTemplate<String, Long> redisCounterTemplate;
    private final RedisTemplate<String, String> stringRedisTemplate;
    private final AchievementKeyBuilder achievementKeyBuilder;

    public RedisCounterService(RedisTemplate<String, Long> redisCounterTemplate,
                               RedisTemplate<String, String> redisStringTemplate,
                               AchievementKeyBuilder achievementKeyBuilder) {
        this.redisCounterTemplate = redisCounterTemplate;
        this.achievementKeyBuilder = achievementKeyBuilder;
        this.stringRedisTemplate = redisStringTemplate;
    }

    @Retryable(
            retryFor = RedisConnectionFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public Long incrementCounter(AchievementCode achievementCode, long userId) {
        log.info("Increment counter title: {}, userId = {}", achievementCode.getName(), userId);
        String key = achievementKeyBuilder.userProgress(achievementCode.getName(), userId);
        return redisCounterTemplate.opsForValue().increment(key, 1);
    }

    @Retryable(
            retryFor = RedisConnectionFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public void decrementCounter(AchievementCode achievementCode, long userId, long delta) {
        log.info("Decrement counter title: {}, userId = {}", achievementCode.getName(), userId);
        String key = achievementKeyBuilder.userProgress(achievementCode.getName(), userId);
        redisCounterTemplate.opsForValue().decrement(key, delta);
    }

    @Retryable(
            retryFor = RedisConnectionFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public void assignAchievement(AchievementCode achievementCode, long userId) {
        log.info("Updating a cache with a completed achievement for a user, title: {}, userId = {}", achievementCode.getName(), userId);
        String key = achievementKeyBuilder.assignAchievement(userId);
        stringRedisTemplate.opsForSet().add(key, achievementCode.getName());
        removeAchievementProgressTracking(achievementCode, userId);
    }

    @Retryable(
            retryFor = RedisConnectionFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public void removeAchievementProgressTracking(AchievementCode achievementCode, long userId) {
        log.info("Removing tracking of the achievement to a user, title: {}, userId = {}", achievementCode.getName(), userId);
        String key = achievementKeyBuilder.userProgress(achievementCode.getName(), userId);
        stringRedisTemplate.delete(key);
    }

    @Retryable(
            retryFor = RedisConnectionFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
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

    @Recover
    public void recover(RedisConnectionFailureException e,
                        AchievementCode code,
                        long userId) {
        log.error("Method failed with params: {}, userId={}", code, userId, e);
    }

    @Recover
    public void recoverDecrement(RedisConnectionFailureException e,
                                 AchievementCode code,
                                 long userId,
                                 long delta) {
        log.error("[DECREMENT] Retry failed: {}, userId={}, delta={}", code, userId, delta, e);
    }
}
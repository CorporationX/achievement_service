package faang.school.achievement.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisCounterServiceImpl extends RedisCounterService {
    public RedisCounterServiceImpl(RedisTemplate<String, Long> redisCounterTemplate, RedisTemplate<String, String> stringRedisTemplate, AchievementKeyBuilder achievementKeyBuilder) {
        super(redisCounterTemplate, stringRedisTemplate, achievementKeyBuilder);
    }
}
package faang.school.achievement.config.redis.jedisconstants;

import java.time.Duration;

public interface JedisConstants {
    int JEDIS_POOL_CONFIG_MAX_TOTAL = 128;
    int JEDIS_POOL_CONFIG_MAX_IDLE = 128;
    int JEDIS_POOL_CONFIG_MIN_IDLE = 16;
    Duration JEDIS_POOL_CONFIG_MAX_WAIT = Duration.ofSeconds(30);

    Duration JEDIS_CLIENT_CONFIG_CONNECT_TIMEOUT = Duration.ofSeconds(5);
    Duration JEDIS_CLIENT_CONFIG_READ_TIMEOUT = Duration.ofSeconds(5);
}

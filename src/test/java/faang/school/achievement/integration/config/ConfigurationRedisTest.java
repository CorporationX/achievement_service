package faang.school.achievement.integration.config;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class ConfigurationRedisTest {

    @Container
    private final static RedisContainer REDIS_CONTAINER = new RedisContainer(
            DockerImageName.parse("redis/redis-stack:latest"))
            .withExposedPorts(6379);

    @BeforeAll
    static void runContainer() {
        REDIS_CONTAINER.start();
    }

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
    }
}

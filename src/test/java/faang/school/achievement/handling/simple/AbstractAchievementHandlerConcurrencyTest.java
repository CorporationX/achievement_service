package faang.school.achievement.handling.simple;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.handling.config.TestAsyncConfig;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@DataJpaTest
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
        TestAchievementHandler.class,
        AchievementCache.class,
        AchievementService.class,
        TestAsyncConfig.class
})
@Transactional(propagation = NOT_SUPPORTED)
public class AbstractAchievementHandlerConcurrencyTest {
    @Autowired
    TestAchievementHandler handler;

    @Autowired
    AchievementService service;

    @Container
    public static PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:13.6");

    @DynamicPropertySource
    static void setPostgresqlContainer(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
    }

    // The increment is not lost and there is no excess
    // The progress is given only once -> ON CONFLICT DO NOTHING
    // The achievement is given only once -> ON CONFLICT DO NOTHING
    // The user got the achievement
    // Retries are performed
    // No additional tests are required for AbstractAchievementHandler.
    @Test
    void ensureIncrementNotLost_atRaceCondition() throws InterruptedException {
        long targetUserId = 10L;
        long performingUserId = 11L;
        long testAchievementId = 9L;

        int threads = 3;
        int start = 1;

        TestEvent event = new TestEvent(targetUserId, performingUserId);

        ExecutorService executor = Executors.newFixedThreadPool(threads);

        CountDownLatch startLatch = new CountDownLatch(start);
        CountDownLatch doneLatch = new CountDownLatch(threads);

        Runnable task = () -> {
            try {
                startLatch.await();
                handler.handle(event);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                doneLatch.countDown();
            }
        };

        executor.submit(task);
        executor.submit(task);
        executor.submit(task);

        startLatch.countDown();

        doneLatch.await();

        executor.shutdown();

        AchievementProgress progress =
                service.getProgress(targetUserId, testAchievementId);

        assertEquals(2, progress.getCurrentPoints());
        assertTrue(service.hasAchievement(targetUserId, testAchievementId));
    }
}
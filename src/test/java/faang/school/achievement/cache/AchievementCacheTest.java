package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
public class AchievementCacheTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Container
    static GenericContainer<?> redisContainer = new GenericContainer<>(DockerImageName.parse("redis:latest"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379));
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.liquibase.enabled", () -> false);
    }

    @Autowired
    private AchievementCache achievementCache;

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    void setUp() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
        achievementRepository.deleteAllInBatch();
    }

    @Test
    @Transactional
    void testGet_whenAchievementNotInCache_thenLoadFromDbAndCache() {
        Achievement achievement = createTestAchievement("TEST", "Test Achievement");
        achievementRepository.save(achievement);

        Achievement result = achievementCache.get("TEST");
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt", "id")
                .isEqualTo(achievement);

        Object cached = redisTemplate.opsForValue().get("achievements::TEST");
        assertThat(cached)
                .isInstanceOf(Achievement.class)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt", "id")
                .isEqualTo(achievement);
    }

    @Test
    void testAddOrUpdate_thenSavesToDbAndUpdatesCache() {
        Achievement achievement = createTestAchievement("NEW", "New Achievement");

        Achievement saved = achievementCache.addOrUpdate(achievement);

        assertThat(achievementRepository.findByTitle("NEW")).isPresent();

        Object cached = redisTemplate.opsForValue().get("achievements::NEW");
        assertThat(cached).isEqualTo(saved);
    }

    @Test
    @Transactional
    void testRemove_thenDeletesFromDbAndEvictsCache() {
        Achievement achievement = createTestAchievement("DELETE_ME", "To be deleted");
        achievementRepository.save(achievement);
        achievementCache.get("DELETE_ME");

        achievementCache.remove("DELETE_ME");

        assertThat(achievementRepository.findByTitle("DELETE_ME")).isEmpty();

        Object cached = redisTemplate.opsForValue().get("achievements::DELETE_ME");
        assertThat(cached).isNull();
    }

    @Test
    @Transactional
    void testInitCache_whenApplicationStarts_thenLoadAllAchievementsToCache() {
        List<Achievement> achievements = List.of(
                createTestAchievement("ACH1", "Achievement 1"),
                createTestAchievement("ACH2", "Achievement 2")
        );
        achievementRepository.saveAll(achievements);

        achievementCache.initCache();

        achievements.forEach(ach -> {
            Object cached = redisTemplate.opsForValue().get("achievements::" + ach.getTitle());
            assertThat(cached).isEqualTo(ach);
        });
    }

    @Test
    void testGet_whenAchievementNotFound_thenThrowException() {
        assertThrows(EntityNotFoundException.class, () -> achievementCache.get("NON_EXISTENT"));
    }

    private Achievement createTestAchievement(String title, String description) {
        return Achievement.builder()
                .title(title)
                .description(description)
                .rarity(Rarity.COMMON)
                .points(10)
                .build();
    }
}

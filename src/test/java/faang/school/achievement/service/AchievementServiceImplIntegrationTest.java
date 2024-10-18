package faang.school.achievement.service;

import faang.school.achievement.model.entity.Achievement;
import faang.school.achievement.model.entity.AchievementProgress;
import faang.school.achievement.model.entity.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.impl.AchievementServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "/scripts/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AchievementServiceImplIntegrationTest {

    @Autowired
    private AchievementServiceImpl achievementService;

    @Autowired
    private UserAchievementRepository userAchievementRepository;

    @Autowired
    private AchievementProgressRepository progressRepository;

    @Autowired
    private AchievementRepository achievementRepository;

    private Achievement achievement;
    private AchievementProgress achievementProgress;

    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:13.3"))
                    .withInitScript("scripts/data.sql");

    @DynamicPropertySource
    static void configProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        //registry.add("spring.jpa.generate-ddl", () -> true);
    }

    @BeforeEach
    void setup(){
        achievement = achievementRepository.findById(1L).get();

        achievementProgress = AchievementProgress.builder()
                .achievement(achievement)
                .currentPoints(5)
                .userId(1L)
                .build();
    }

    @Test
    void testCreateProgressOk() {
        achievementService.createProgress(1L, 2L);

        assertNotNull(progressRepository.findByUserIdAndAchievementId(1L, 2L));
    }

    @Test
    void hasAchievementOk() {

        UserAchievement ua = UserAchievement.builder()
                .achievement(achievement)
                .userId(1L)
                .build();

        userAchievementRepository.save(ua);

        assertTrue(achievementService.hasAchievement(1L, 1L));
    }

    @Test
    void getProgressOk(){
        AchievementProgress achievementProgress = AchievementProgress.builder()
                .achievement(achievement)
                .currentPoints(5)
                .userId(1L)
                .build();

        progressRepository.save(achievementProgress);

        assertEquals(5, achievementService.getProgress(1L, 1L).getCurrentPoints());
    }

    @Test
    void getProgressError(){
        assertThrows(EntityNotFoundException.class,
                () -> achievementService.getProgress(1L, 1L));
    }

    @Test
    void testGiveAchievementOk(){
        achievementService.giveAchievement(1L, 1L);

        assertTrue(userAchievementRepository.existsById(1L));
    }

    @Test
    void testSaveProgressOk(){
        achievementService.saveProgress(achievementProgress);

        assertTrue(progressRepository.findByUserIdAndAchievementId(1L, 1L).isPresent());
    }
}

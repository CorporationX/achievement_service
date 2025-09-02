package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementType;
import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext
@Testcontainers
@ActiveProfiles("test")
@SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
class AchievementServiceIntTest {
    @Autowired
    private AchievementService service;
    @Autowired
    private AchievementRepository achievementRepository;
    @Autowired
    private UserAchievementRepository userAchievementRepository;
    @Autowired
    private AchievementProgressRepository achievementProgressRepository;
    @Autowired
    private AchievementMapper mapper;

    @Container
    public static PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:13.3");
    private static final String NOT_EXISTS_TITLE = "UNKNOWN";
    private static final Long EXPERT_ID = 3L;
    private static final Long PROGRESS_ID = 2L;
    private static final Long HAS_ACHIEVEMENT_USER_ID = 1L;
    private static final Long NO_PROGRESS_USER_ID = 2L;
    private static final Long NO_ACHIEVEMENT_USER_ID = 3L;
    private static final Long NO_PROGRESS_TOO_USER_ID = 4L;

    @DynamicPropertySource
    static void propertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
    }

    @Test
    @DisplayName("Успешное получение достижения по названию")
    void positive_shouldFindAchievementByTitle() {
        AchievementDto actual = service.findByTitle(AchievementType.EXPERT.name());

        assertNotNull(actual);
        assertEquals(AchievementType.EXPERT, actual.title());
    }

    @Test
    @DisplayName("Вернет true, если достижение есть у пользователя")
    void positive_whenHasAchievement_returnsTrue() {
        boolean actual = service.hasAchievement(HAS_ACHIEVEMENT_USER_ID, EXPERT_ID);

        assertTrue(actual);
    }

    @Test
    @DisplayName("Вернет false, если достижения нет у пользователя")
    void positive_whenHasNotAchievement_returnsFalse() {
        boolean actual = service.hasAchievement(NO_PROGRESS_USER_ID, EXPERT_ID);

        assertFalse(actual);
    }

    @Test
    @DisplayName("Успешное создание прогресса достижения, если нет")
    void positive_ifProgressNotExists_createsProgress() {
        List<AchievementProgress> expected = achievementProgressRepository.findByUserId(NO_PROGRESS_USER_ID);

        service.createProgressIfNecessary(NO_PROGRESS_USER_ID, EXPERT_ID);
        List<AchievementProgress> actual = achievementProgressRepository.findByUserId(NO_PROGRESS_USER_ID);

        assertTrue(expected.isEmpty());
        assertFalse(actual.isEmpty());
    }

    @Test
    @DisplayName("Не создаст прогресс достижения, если он есть")
    void positive_ifProgressExists_notCreatesProgress() {
        List<AchievementProgress> expected = achievementProgressRepository.findByUserId(HAS_ACHIEVEMENT_USER_ID);

        service.createProgressIfNecessary(HAS_ACHIEVEMENT_USER_ID, EXPERT_ID);
        List<AchievementProgress> actual = achievementProgressRepository.findByUserId(HAS_ACHIEVEMENT_USER_ID);

        assertEquals(expected.size(), actual.size());
    }

    @Test
    @DisplayName("Успешное получение прогресса")
    void positive_shouldFindProgress() {
        AchievementProgress actual = service.getProgress(HAS_ACHIEVEMENT_USER_ID, EXPERT_ID);

        assertNotNull(actual);
    }

    @Test
    @DisplayName("Успешная выдача достижения пользователю")
    void positive_shouldGiveAchievement() {
        service.giveAchievement(EXPERT_ID, NO_ACHIEVEMENT_USER_ID);
        boolean actual =
                userAchievementRepository.existsByUserIdAndAchievementId(NO_ACHIEVEMENT_USER_ID, EXPERT_ID);

        assertTrue(actual);
    }

    @Test
    @DisplayName("Успешное обновление points прогресса достижения")
    void positive_shouldUpdateProgress() {
        AchievementProgress expected = achievementProgressRepository.findById(PROGRESS_ID).get();
        expected.increment();
        long expectedPoints = expected.getCurrentPoints();

        long actualPoints = service.incrementAndGetPointsById(PROGRESS_ID);

        AchievementProgress actual = achievementProgressRepository.findById(PROGRESS_ID).get();

        assertEquals(expectedPoints, actual.getCurrentPoints());
    }

    @Test
    @DisplayName("Ошибка получения достижения по названию - не надено")
    void negative_whenNotFoundByTitle_throwsError() {
        String expectedMessage = "Achievement not found by title=" + NOT_EXISTS_TITLE;

        String actualMessage = assertThrows(EntityNotFoundException.class,
                                            () -> service.findByTitle(NOT_EXISTS_TITLE)).getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Ошибка получения прогрусса достижения - не надено")
    void negative_whenNotFoundProgress_throwsError() {
        String expectedMessage = String.format("AchievementProgress not found by userId=%d и achievementId=%d",
                                               NO_PROGRESS_TOO_USER_ID, EXPERT_ID);
        String actualMessage =
                assertThrows(EntityNotFoundException.class,
                             () -> service.getProgress(NO_PROGRESS_TOO_USER_ID, EXPERT_ID)).getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
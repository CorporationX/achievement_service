package faang.school.achievement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.mapper.AchievementMapperImpl;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.Rarity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
public class AchievementRequestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AchievementMapperImpl achievementMapper;

    private static final int REDIS_PORT = 6379;

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withInitScript("dbtest/achievement_V001__initial.sql")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    @Container
    public static GenericContainer<?> redisContainer = new GenericContainer<>("redis:latest")
            .withExposedPorts(REDIS_PORT);

    private Achievement achievement1;
    private Achievement achievement2;

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(REDIS_PORT).toString());
    }

    @BeforeEach
    void setUp() {
        achievement1 = Achievement.builder()
                .id(4L)
                .title("SENSEI")
                .description("For 30 mentees")
                .rarity(Rarity.LEGENDARY)
                .points(20L)
                .build();

        achievement2 = Achievement.builder()
                .id(3L)
                .title("EXPERT")
                .description("For 1000 comments")
                .rarity(Rarity.UNCOMMON)
                .points(5L)
                .build();
    }

    @Test
    void testGetAllAchievementsByFilter() throws Exception {
        mockMvc.perform(get("/api/v1/achievement")
                        .header("x-user-id", 1L)
                        .param("title", "SENSEI")
                        .param("rarity", "LEGENDARY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("SENSEI")))
                .andExpect(jsonPath("$[0].rarity", is("LEGENDARY")))
                .andReturn();
    }

    @Test
    void testGetAchievementsByFilterNoResults() throws Exception {
        mockMvc.perform(get("/api/v1/achievement")
                        .header("x-user-id", "1")
                        .param("title", "NON_EXISTENT_TITLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testGetAchievementByUserId() throws Exception {
        AchievementDto correctResult = achievementMapper.toDto(achievement1);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/achievement")
                .header("x-user-id", 1L));
        String jsonResult = response.andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        AchievementDto[] resultArray = objectMapper.readValue(jsonResult, AchievementDto[].class);

        AchievementDto result = Arrays.stream(resultArray)
                .filter(a -> a.getId() == 4L)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Achievement with id 4 not found"));

        assertEquals(correctResult, result);
    }

    @Test
    void testGetAchievementByIdSuccessfully() throws Exception {
        AchievementDto correctResult = achievementMapper.toDto(achievement1);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/achievement/{achievementId}", 4L)
                .header("x-user-id", 1L));
        String jsonResult = response.andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        AchievementDto result = objectMapper.readValue(jsonResult, AchievementDto.class);

        assertEquals(correctResult, result);
    }

    @Test
    void testGetAchievementNotFoundAchievement() throws Exception {
        long achievementId = -1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/achievement/{achievementId}", achievementId)
                        .header("x-user-id", 1L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("getAchievementById.id: must be greater than 0"));
    }

    @Test
    void testGetUnearnedAchievements() throws Exception {
        Long userId = 1L;
        AchievementDto correctResult = achievementMapper.toDto(achievement2);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/achievement/user/{userId}/unearned", userId)
                .header("x-user-id", 1L));
        String jsonResult = response.andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        AchievementDto[] resultArray = objectMapper.readValue(jsonResult, AchievementDto[].class);
        AchievementDto result = resultArray[0];

        assertEquals(1, resultArray.length);
        assertEquals(correctResult, result);
    }
}

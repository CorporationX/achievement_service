package faang.school.achievement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.mapper.AchievementMapperImpl;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.Rarity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
public class AchievementControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AchievementMapperImpl achievementMapper;

    private static final int REDIS_PORT = 6379;

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withInitScript("script/achievement_V001__initial.sql")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    @Container
    public static GenericContainer<?> redisContainer = new GenericContainer<>("redis:latest")
            .withExposedPorts(REDIS_PORT);

    @Value("${server.name}")
    private String serviceName;

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
        achievement2 = Achievement.builder()
                .id(3L)
                .title("EXPERT")
                .description("For 1000 comments")
                .rarity(Rarity.UNCOMMON)
                .points(5L)
                .build();
        achievement1 = Achievement.builder()
                .id(4L)
                .title("SENSEI")
                .description("For 30 mentees")
                .rarity(Rarity.LEGENDARY)
                .points(20L)
                .build();
    }

    @Test
    void testGetAchievementsByFilter() throws Exception {
        AchievementDto correctResult = achievementMapper.toDto(achievement1);
        AchievementFilterDto filter = AchievementFilterDto
                .builder()
                .title("SENSEI")
                .rarity(Rarity.LEGENDARY)
                .build();
        String jsonFilter = objectMapper.writeValueAsString(filter);

        String jsonResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/achievement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-user-id", 1L)
                        .content(jsonFilter))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AchievementDto[] resultArray = objectMapper.readValue(jsonResult, AchievementDto[].class);
        AchievementDto result = resultArray[0];

        assertEquals(1, resultArray.length);
        assertEquals(correctResult, result);
    }

    @Test
    void testGetAchievementByUserId() throws Exception {
        AchievementDto correctResult = achievementMapper.toDto(achievement1);
        correctResult.setProgresses(Collections.emptyList());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/achievement")
                .header("x-user-id", 1L));
        String jsonResult = response.andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        AchievementDto[] resultArray = objectMapper.readValue(jsonResult, AchievementDto[].class);
        AchievementDto result = resultArray[0];

        assertEquals(2, resultArray.length);
        assertEquals(correctResult, result);
    }

    @Test
    void testGetAchievement_FoundAchievement() throws Exception {
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
    void testGetAchievement_NotFoundAchievement() throws Exception {
        long achievementId = -1L;
        String message = "Achievement %d not found".formatted(achievementId);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/achievement/{achievementId}", achievementId)
                        .header("x-user-id", 1L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.serviceName").value(serviceName))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.globalMessage").value(message));
    }

    @Test
    void testGetNotReceivedAchievements() throws Exception {
        AchievementDto correctResult = achievementMapper.toDto(achievement2);
        AchievementProgressDto progress = new AchievementProgressDto(1L, 7L, 1L);
        correctResult.setProgresses(Collections.singletonList(progress));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/achievement/not-received")
                .header("x-user-id", 1L));
        String jsonResult = response.andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        AchievementDto[] resultArray = objectMapper.readValue(jsonResult, AchievementDto[].class);
        AchievementDto result = resultArray[2];

        assertEquals(6, resultArray.length);
        assertEquals(correctResult, result);
    }
}

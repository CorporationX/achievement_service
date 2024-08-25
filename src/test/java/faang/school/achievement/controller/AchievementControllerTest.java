package faang.school.achievement.controller;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AchievementControllerTest {

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private AchievementController achievementController;

    private MockMvc mockMvc;
    private AchievementFilterDto achievementFilterDto;
    private String achievementFilterDtoJson;
    private long userId;
    private long achievementId;
    private int offset;
    private int limit;
    private String sortField;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        userId = 1L;
        achievementId = 2L;
        offset = 1;
        limit = 10;
        sortField = "title";
        mockMvc = MockMvcBuilders.standaloneSetup(achievementController).build();
        achievementFilterDto = new AchievementFilterDto();
        achievementFilterDtoJson = objectMapper.writeValueAsString(achievementFilterDto);
    }

    @Test
    @DisplayName("testing getUserAchievements controller")
    void testGetUserAchievements() throws Exception {
        mockMvc.perform(get("/api/v1/achievement/user", userId))
                .andExpect(status().isOk());
        verify(achievementService, times(1)).getAchievementsByUserId();
    }

    @Test
    @DisplayName("testing getAchievement controller")
    void testGetAchievement() throws Exception {
        mockMvc.perform(get("/api/v1/achievement/{achievementId}", achievementId))
                .andExpect(status().isOk());
        verify(achievementService, times(1)).getAchievementById(achievementId);
    }

    @Test
    @DisplayName("testing getUserNotAttainedAchievements controller")
    void testGetUserNotAttainedAchievements() throws Exception {
        mockMvc.perform(get("/api/v1/achievement/user/not-attained"))
                .andExpect(status().isOk());
        verify(achievementService, times(1)).getUserNotAttainedAchievements();
    }
}
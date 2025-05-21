package faang.school.achievement.controller;

import faang.school.achievement.dto.*;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.service.AchievementRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest()
@ContextConfiguration(classes = {AchievementRequestController.class})
class AchievementRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AchievementRequestService achievementRequestService;

    @Test
    void getAllAchievements() throws Exception {
        AchievementDto dto = AchievementDto.builder()
                .id(1L)
                .title("Achievement 1")
                .description("Desc")
                .rarity(Rarity.COMMON)
                .build();

        when(achievementRequestService.getAllAchievements(any())).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/achievement"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Achievement 1"))
                .andExpect(jsonPath("$[0].description").value("Desc"))
                .andExpect(jsonPath("$[0].rarity").value("COMMON"));
    }

    @Test
    void getUserAchievements() throws Exception {
        AchievementDto achievementDto = AchievementDto.builder()
                .id(100L)
                .title("test title")
                .description("test descr")
                .rarity(Rarity.LEGENDARY)
                .build();

        UserAchievementDto dto = UserAchievementDto.builder()
                .id(1L)
                .userId(1L)
                .achievement(achievementDto)
                .createdAt(LocalDateTime.now())
                .build();

        when(achievementRequestService.getUserAchievements(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/achievement/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].achievement.id").value(100L))
                .andExpect(jsonPath("$[0].achievement.title").value("test title"))
                .andExpect(jsonPath("$[0].achievement.description").value("test descr"))
                .andExpect(jsonPath("$[0].achievement.rarity").value("LEGENDARY"));
    }

    @Test
    void getAchievementById() throws Exception {
        AchievementDto dto = AchievementDto.builder()
                .id(1L)
                .title("Achievement 1")
                .description("Description")
                .rarity(Rarity.RARE)
                .build();

        when(achievementRequestService.getAchievementById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/achievement/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Achievement 1"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.rarity").value("RARE"));
    }

    @Test
    void getUserUnearnedAchievements() throws Exception {
        AchievementProgressDto dto = AchievementProgressDto.builder()
                .id(10L)
                .title("Unearned")
                .description("Need more points")
                .rarity(Rarity.EPIC)
                .currentsPoints(50)
                .totalPoints(100)
                .build();

        when(achievementRequestService.getUserUnearnedAchievements(10L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/achievement/user/10/unearned"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(10L))
                .andExpect(jsonPath("$[0].title").value("Unearned"))
                .andExpect(jsonPath("$[0].description").value("Need more points"))
                .andExpect(jsonPath("$[0].rarity").value("EPIC"))
                .andExpect(jsonPath("$[0].currentsPoints").value(50))
                .andExpect(jsonPath("$[0].totalPoints").value(100));
    }
}
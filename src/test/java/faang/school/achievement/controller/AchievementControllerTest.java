package faang.school.achievement.controller;

import faang.school.achievement.config.context.UserContext;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.exception.AchievementNotFoundException;
import faang.school.achievement.exception.EmptyFilterException;
import faang.school.achievement.exception.ExceptionMessage;
import faang.school.achievement.exception.GlobalExceptionHandler;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {AchievementController.class})
@WebMvcTest
public class AchievementControllerTest {

    private final static String BASE_URL = "/achievement/";
    private final static String FIND_URL = BASE_URL + "{id}";
    private final static String FIND_COMPLETED_URL = BASE_URL + "completed";
    private final static String FIND_PROCESS_URL = BASE_URL + "process";
    private final static String FIND_ALL_URL = BASE_URL + "all";
    private final static String FILTER_URL = BASE_URL + "filter";

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @MockBean
    GlobalExceptionHandler exceptionHandler;

    @MockBean
    UserContext userContext;

    @MockBean
    AchievementService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testFindCompletedAchievements() throws Exception {
        List<UserAchievementDto> achievementDtos = List.of(createUserAchievementDto(), createUserAchievementDto());
        when(userContext.getUserId()).thenReturn(1L);
        when(service.findAchievementsByUserId(any(Long.class))).thenReturn(achievementDtos);

        mockMvc.perform(get(FIND_COMPLETED_URL))
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(achievementDtos)))
                .andExpect(status().isOk());
    }

    @Test
    void testFindProcessAchievements() throws Exception {
        List<AchievementProgressDto> achievementDtos = List.of(createAchievementProgressDto(), createAchievementProgressDto());
        when(userContext.getUserId()).thenReturn(1L);
        when(service.findProcessingAchievementsByUserId(any(Long.class))).thenReturn(achievementDtos);

        mockMvc.perform(get(FIND_PROCESS_URL))
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(achievementDtos)))
                .andExpect(status().isOk());
    }

    @Test
    void testFindAllAchievements() throws Exception {
        List<AchievementDto> achievementDtos = List.of(createAchievementDto(), createAchievementDto());
        when(service.findAll()).thenReturn(achievementDtos);

        mockMvc.perform(get(FIND_ALL_URL))
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(achievementDtos)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Negative: error when filter is empty")
    void testFindFilteredAchievementsNegative() throws Exception {
        when(service.findFilteredAchievements(any(AchievementFilterDto.class)))
                .thenThrow(EmptyFilterException.class);
        when(exceptionHandler.handleEmptyFilterException(any(EmptyFilterException.class)))
                .thenReturn(ExceptionMessage.EMPTY_FILTER.getMessage());

        mockMvc.perform(post(FILTER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(createAchievementFilterDto())))
                .andExpect(content().string(ExceptionMessage.EMPTY_FILTER.getMessage()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFindFilteredAchievementsSuccess() throws Exception {
        List<AchievementDto> achievementDtos = List.of(createAchievementDto(), createAchievementDto());
        when(service.findFilteredAchievements(any(AchievementFilterDto.class))).thenReturn(achievementDtos);

        mockMvc.perform(post(FILTER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(createAchievementFilterDto())))
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(achievementDtos)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Negative: error when achievement not found")
    void testFindByIdNegative() throws Exception {
        long idForSearch = 1;
        when(service.findById(any(Long.class))).thenThrow(AchievementNotFoundException.class);
        when(exceptionHandler.handleAchievementNotFoundException(any(AchievementNotFoundException.class)))
                .thenReturn(ExceptionMessage.ACHIEVEMENT_NOT_FOUND.formatMessage(idForSearch));

        mockMvc.perform(get(FIND_URL, idForSearch))
                .andExpect(content().string(ExceptionMessage.ACHIEVEMENT_NOT_FOUND.formatMessage(idForSearch)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFindByIdSuccess() throws Exception {
        when(service.findById(any(Long.class))).thenReturn(createAchievementDto());

        mockMvc.perform(get(FIND_URL, "1"))
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(createAchievementDto())))
                .andExpect(status().isOk());
    }

    private UserAchievementDto createUserAchievementDto() {
        return new UserAchievementDto(1L, createAchievementDto());
    }

    private AchievementDto createAchievementDto() {
        return AchievementDto.builder()
                .id(1L)
                .title("test title")
                .description("test description")
                .rarity(Rarity.COMMON)
                .points(50)
                .build();
    }

    private AchievementProgressDto createAchievementProgressDto() {
        return new AchievementProgressDto(1L, 20, createAchievementDto());
    }

    private AchievementFilterDto createAchievementFilterDto() {
        return new AchievementFilterDto("test", "test", Rarity.COMMON);
    }

}

package faang.school.achievement.controller;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.service.interfaces.AchievementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementControllerTest {

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private AchievementController achievementController;

    private final long USER_ID = 1L;

    @Test
    void testGetAchievementsWhenReturnFilteredAchievements() {
        AchievementFilterDto filterDto = new AchievementFilterDto();
        List<AchievementDto> expected = List.of(new AchievementDto());

        when(achievementService.getFilteredAchievements(filterDto)).thenReturn(expected);

        ResponseEntity<List<AchievementDto>> response =
                achievementController.getAchievements(filterDto, USER_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void testGetUserAchievementsWhenReturnAchievementsForUser() {
        List<UserAchievementDto> expected = List.of(new UserAchievementDto());

        when(achievementService.getAchievementsByUserId(USER_ID)).thenReturn(expected);

        ResponseEntity<List<UserAchievementDto>> response =
                achievementController.getUserAchievments(USER_ID, USER_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void testGetAchievementWhenReturnAchievementById() {
        long achievementId = 42L;
        AchievementDto expected = new AchievementDto();

        when(achievementService.getAchievementById(achievementId)).thenReturn(expected);

        ResponseEntity<AchievementDto> response =
                achievementController.getAchievement(achievementId, USER_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void testGetUserAchievementProgressWhenReturnProgressForUser() {
        List<UserAchievementDto> expected = List.of(new UserAchievementDto());

        when(achievementService.getAchievementsProgressByUserId(USER_ID)).thenReturn(expected);

        ResponseEntity<List<UserAchievementDto>> response =
                achievementController.getUserAchievementProgress(USER_ID, USER_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }
}

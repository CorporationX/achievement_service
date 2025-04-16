package faang.school.achievement.service;

import faang.school.achievement.exception.NotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserAchievementServiceTest {

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private UserAchievementService userAchievementService;

    private long userId;
    private long achievementId;
    private Achievement achievement;
    private UserAchievement userAchievement;

    @BeforeEach
    void setUp() {
        userId = 1L;
        achievementId = 1L;
        achievement = new Achievement();
        achievement.setId(achievementId);
        achievement.setTitle("Test Achievement");

        userAchievement = UserAchievement.builder()
                .userId(userId)
                .achievement(achievement)
                .build();
    }

    @Test
    void testHasAchievementExists() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(true);

        boolean result = userAchievementService.hasAchievement(userId, achievementId);

        assertTrue(result);
        verify(userAchievementRepository).existsByUserIdAndAchievementId(userId, achievementId);
        verifyNoMoreInteractions(userAchievementRepository, achievementService);
    }

    @Test
    void testHasAchievementNotExists() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(false);

        boolean result = userAchievementService.hasAchievement(userId, achievementId);

        assertFalse(result);
        verify(userAchievementRepository).existsByUserIdAndAchievementId(userId, achievementId);
        verifyNoMoreInteractions(userAchievementRepository, achievementService);
    }

    @Test
    void testGiveAchievementSuccess() {
        when(achievementService.getAchievementById(achievementId)).thenReturn(achievement);
        when(userAchievementRepository.save(any(UserAchievement.class))).thenReturn(userAchievement);

        userAchievementService.giveAchievement(userId, achievementId);

        verify(achievementService).getAchievementById(achievementId);
        verify(userAchievementRepository).save(any(UserAchievement.class));
        verifyNoMoreInteractions(userAchievementRepository, achievementService);
    }

    @Test
    void testGiveAchievementAchievementNotFound() {
        when(achievementService.getAchievementById(achievementId))
                .thenThrow(new NotFoundException("Achievement was not found"));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userAchievementService.giveAchievement(userId, achievementId);
        });

        assertEquals("Achievement was not found", exception.getMessage());
        verify(achievementService).getAchievementById(achievementId);
        verifyNoInteractions(userAchievementRepository);
    }
}
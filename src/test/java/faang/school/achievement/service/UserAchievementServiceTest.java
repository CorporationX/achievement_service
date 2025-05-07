package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.achievement.AchievementService;
import faang.school.achievement.service.userachievement.DefaultUserAchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserAchievementServiceTest {

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private DefaultUserAchievementService userAchievementService;

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
    void testAssignAchievementToUserSuccess() {
        when(achievementService.getAchievementById(achievementId)).thenReturn(achievement);
        when(userAchievementRepository.save(any(UserAchievement.class))).thenReturn(userAchievement);

        userAchievementService.assignAchievementToUser(userId, achievementId);

        verify(achievementService).getAchievementById(achievementId);
        verify(userAchievementRepository).save(any(UserAchievement.class));
        verifyNoMoreInteractions(userAchievementRepository, achievementService);
    }

    @Test
    void testAssignAchievementAchievementToUserNotFound() {
        when(achievementService.getAchievementById(achievementId))
                .thenThrow(new NoSuchElementException("Achievement was not found"));

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            userAchievementService.assignAchievementToUser(userId, achievementId);
        });

        assertEquals("Achievement was not found", exception.getMessage());
        verify(achievementService).getAchievementById(achievementId);
        verifyNoInteractions(userAchievementRepository);
    }
}
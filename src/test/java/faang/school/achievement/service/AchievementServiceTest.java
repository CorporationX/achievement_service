package faang.school.achievement.service;

import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {
    private static final long USER_ID = 1L;
    private static final long ACHIEVEMENT_ID = 1L;
    private static final String ACHIEVEMENT_TITLE = "Test Achievement";

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @InjectMocks
    private AchievementService achievementService;

    @Test
    void testHasAchievement_ShouldReturnTrueIfAchievementExists() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID)).thenReturn(true);

        boolean result = achievementService.hasAchievement(USER_ID, ACHIEVEMENT_ID);

        assertTrue(result);
        verify(userAchievementRepository).existsByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    void testHasAchievement_ShouldReturnFalseIfAchievementDoesNotExist() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID)).thenReturn(false);

        boolean result = achievementService.hasAchievement(USER_ID, ACHIEVEMENT_ID);

        assertFalse(result);
        verify(userAchievementRepository).existsByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    void testGetProgress_ShouldReturnProgressIfExists() {
        Achievement achievement = Achievement.builder()
                .id(ACHIEVEMENT_ID)
                .title(ACHIEVEMENT_TITLE)
                .build();
        AchievementProgress progress = AchievementProgress.builder()
                .achievement(achievement)
                .userId(USER_ID)
                .currentPoints(5)
                .build();
        when(achievementProgressRepository.findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID))
                .thenReturn(Optional.of(progress));

        AchievementProgress result = achievementService.getProgress(USER_ID, ACHIEVEMENT_ID);

        assertNotNull(result);
        assertEquals(progress, result);
        verify(achievementProgressRepository).findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    void testGetProgress_ShouldThrowEntityNotFoundExceptionIfProgressDoesNotExist() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> achievementService.getProgress(USER_ID, ACHIEVEMENT_ID));
        assertEquals("Прогресс по достижению с id " + USER_ID + " не существует", exception.getMessage());
        verify(achievementProgressRepository).findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    void testGiveAchievement_ShouldSaveUserAchievement() {
        Achievement achievement = Achievement.builder()
                .id(ACHIEVEMENT_ID)
                .title(ACHIEVEMENT_TITLE)
                .build();

        achievementService.giveAchievement(USER_ID, achievement);

        verify(userAchievementRepository).save(any(UserAchievement.class));
    }

    @Test
    void testCreateProgressIfNecessary_ShouldCallRepositoryMethod() {
        achievementService.createProgressIfNecessary(USER_ID, ACHIEVEMENT_ID);

        verify(achievementProgressRepository).createProgressIfNecessary(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    void testSaveProgress_ShouldCallRepositoryMethod() {
        Achievement achievement = Achievement.builder()
                .id(ACHIEVEMENT_ID)
                .title(ACHIEVEMENT_TITLE)
                .build();
        AchievementProgress progress = AchievementProgress.builder()
                .achievement(achievement)
                .userId(USER_ID)
                .currentPoints(5)
                .build();

        achievementService.saveProgress(progress);

        verify(achievementProgressRepository).save(progress);
    }
}
package faang.school.achievement.service;

import faang.school.achievement.exception.NotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.service.achievement_progress.AchievementProgressService;
import faang.school.achievement.service.achievement_progress.AchievementProgressServiceImpl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementProgressServiceTest {

    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private AchievementProgressService achievementProgressService;

    private final long userId = 1L;
    private final long achievementId = 2L;
    private final long progressId = 3L;

    private AchievementProgress achievementProgress;

    @Mock
    private Achievement achievement;

    @BeforeEach
    void setUp() throws Exception {
        Field entityManagerField = AchievementProgressServiceImpl.class.getDeclaredField("entityManager");
        entityManagerField.setAccessible(true);
        entityManagerField.set(achievementProgressService, entityManager);

        achievement = new Achievement();
        achievement.setId(achievementId);

        achievementProgress = new AchievementProgress();
        achievementProgress.setId(progressId);
        achievementProgress.setUserId(userId);
        achievementProgress.setAchievement(achievement);
        achievementProgress.setCurrentPoints(5);
    }

    @Test
    void testCreateProgressIfNecessaryShouldCallRepository() {
        achievementProgressService.createProgressIfNecessary(userId, achievementId);

        verify(achievementProgressRepository).createProgressIfNecessary(userId, achievementId);
    }

    @Test
    void testGetProgressShouldReturnProgressWhenExists() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.of(achievementProgress));

        AchievementProgress result = achievementProgressService.getProgress(userId, achievementId);

        assertNotNull(result);
        assertEquals(progressId, result.getId());
        assertEquals(userId, result.getUserId());
        assertEquals(achievementId, result.getAchievement().getId());
        assertEquals(5, result.getCurrentPoints());
        verify(achievementProgressRepository).findByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    void testGetProgressShouldThrowNotFoundExceptionWhenNotExists() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                achievementProgressService.getProgress(userId, achievementId));

        assertEquals(String.format("AchievementProgress %d for the user %d was not found",
                achievementId, userId), exception.getMessage());
        verify(achievementProgressRepository).findByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    void testProgressIncrementSuccess() {
        long progressId = 1L;
        AchievementProgress progress = new AchievementProgress();
        progress.setId(progressId);

        when(entityManager.find(AchievementProgress.class, progressId)).thenReturn(progress);

        AchievementProgress result = achievementProgressService.progressIncrement(progressId);

        assertNotNull(result);
        assertEquals(progressId, result.getId());
        verify(entityManager).find(AchievementProgress.class, progressId);
        verifyNoMoreInteractions(entityManager);
    }

    @Test
    void testProgressIncrementNotFound() {
        long progressId = 1L;

        when(entityManager.find(AchievementProgress.class, progressId)).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            achievementProgressService.progressIncrement(progressId);
        });

        assertEquals("Achievement progress with id 1 not found", exception.getMessage());
        verify(entityManager).find(AchievementProgress.class, progressId);
    }
}
package faang.school.achievement.handlers;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.DataForTestsObtainAchievements;
import faang.school.achievement.service.LockedOperationRunnable;
import faang.school.achievement.service.TransactionalLockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpertAchievementHandlerTest extends DataForTestsObtainAchievements {

    @Mock
    private AchievementService achievementService;

    @Mock
    private TransactionalLockService lockService;

    @Mock
    private AchievementCache achievementCache;

    // Убрали @InjectMocks, чтобы создавать вручную
    private ExpertAchievementHandler handler;

    private Achievement achievement;

    @BeforeEach
    void setUp() {
        handler = new ExpertAchievementHandler(achievementService, ACHIEVEMENT_NAME, achievementCache, lockService);

        achievement = new Achievement();
        achievement.setId(ACHIEVEMENT_ID);
        achievement.setTitle(ACHIEVEMENT_NAME);
        achievement.setPoints(POINTS_THRESHOLD);

        lenient().doAnswer(invocation -> {
            LockedOperationRunnable runnable = invocation.getArgument(1);
            runnable.run();
            return null;
        }).when(lockService).runWithTransactionAndLock(anyLong(), any(LockedOperationRunnable.class));
    }

    @Test
    void handle_UserAlreadyHasAchievementShouldDoNothing() {
        when(achievementCache.getByTitle(ACHIEVEMENT_NAME)).thenReturn(Optional.of(achievement));
        when(achievementService.hasAchievement(AUTHOR_ID, ACHIEVEMENT_ID)).thenReturn(true);

        handler.handle(event);

        verify(achievementService).hasAchievement(AUTHOR_ID, ACHIEVEMENT_ID);
        verify(achievementService, never()).createProgressIfNecessary(anyLong(), anyLong());
        verify(achievementService, never()).saveProgress(any());
    }

    @Test
    void handle_UserDoesNotHaveAchievementShouldIncrementProgress() {
        long currentPoints = 2L;
        AchievementProgress progress = new AchievementProgress();
        progress.setCurrentPoints(currentPoints);

        when(achievementCache.getByTitle(ACHIEVEMENT_NAME)).thenReturn(Optional.of(achievement));
        when(achievementService.hasAchievement(AUTHOR_ID, ACHIEVEMENT_ID)).thenReturn(false);
        when(achievementService.getProgress(AUTHOR_ID, ACHIEVEMENT_ID)).thenReturn(Optional.of(progress));

        handler.handle(event);

        verify(achievementService).createProgressIfNecessary(AUTHOR_ID, ACHIEVEMENT_ID);
        assertEquals(currentPoints + 1, progress.getCurrentPoints()); // 3
        verify(achievementService).saveProgress(progress);
        verify(achievementService, never()).giveAchievement(anyLong(), anyLong());
    }

    @Test
    void handle_UserReachesThresholdShouldGiveAchievement() {
        long currentPoints = 4L;
        AchievementProgress progress = new AchievementProgress();
        progress.setCurrentPoints(currentPoints);

        when(achievementCache.getByTitle(ACHIEVEMENT_NAME)).thenReturn(Optional.of(achievement));
        when(achievementService.hasAchievement(AUTHOR_ID, ACHIEVEMENT_ID)).thenReturn(false);
        when(achievementService.getProgress(AUTHOR_ID, ACHIEVEMENT_ID)).thenReturn(Optional.of(progress));

        handler.handle(event);

        assertEquals(POINTS_THRESHOLD, progress.getCurrentPoints());
        verify(achievementService).saveProgress(progress);
        verify(achievementService).giveAchievement(AUTHOR_ID, ACHIEVEMENT_ID);
    }

    @Test
    void handle_ProgressNotFoundShouldThrowException() {
        when(achievementCache.getByTitle(ACHIEVEMENT_NAME)).thenReturn(Optional.of(achievement));
        when(achievementService.hasAchievement(AUTHOR_ID, ACHIEVEMENT_ID)).thenReturn(false);
        when(achievementService.getProgress(AUTHOR_ID, ACHIEVEMENT_ID)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> handler.handle(event));
    }

    @Test
    void getHandlerExecutionTimeShouldReturnCorrectTime() {
        assertEquals(5000L, handler.getHandlerExecutionTime());
    }
}
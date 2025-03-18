package faang.school.achievement.event_handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.exception.EventHandlingException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractEventHandlerTest {
    private static final long ACHIEVEMENT_ID = 1L;
    private static final long USER_ID = 12L;
    private static final long ACHIEVEMENT_PROGRESS_ID = 1L;
    private static final String TITLE_ACHIEVEMENT = "Test Achievement";

    public static class AbstractEventHandlerStub<T> extends AbstractEventHandler<T> {
        public AbstractEventHandlerStub(AchievementCache achievementCache,
                                        AchievementService achievementService,
                                        String achievementTitle) {
            super(achievementCache, achievementService, achievementTitle);
        }

        @Override
        protected long getUserId(T event) {
            return USER_ID;
        }
    }

    @Mock
    private AchievementCache achievementCache;

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private AbstractEventHandlerStub<Object> eventHandler;

    @BeforeEach
    void setUp() {
        eventHandler = new AbstractEventHandlerStub<>(achievementCache,
                achievementService,
                TITLE_ACHIEVEMENT);
    }

    @Test
    void testHandleEvent_ShouldCreateProgressAndNotGiveAchievement() {
        AchievementProgress achievementProgress = AchievementProgress.builder()
                .id(ACHIEVEMENT_PROGRESS_ID)
                .userId(USER_ID)
                .currentPoints(1)
                .build();
        Achievement achievement = Achievement.builder()
                .id(ACHIEVEMENT_ID)
                .title(TITLE_ACHIEVEMENT)
                .points(10)
                .build();

        when(achievementCache.get(TITLE_ACHIEVEMENT)).thenReturn(achievement);
        when(achievementService.hasAchievement(USER_ID, ACHIEVEMENT_ID)).thenReturn(false);
        when(achievementService.getProgress(USER_ID, ACHIEVEMENT_ID))
                .thenReturn(achievementProgress);

        eventHandler.handleEvent(new Object());

        verify(achievementService).createProgressIfNecessary(USER_ID, ACHIEVEMENT_ID);
        verify(achievementService).getProgress(USER_ID, ACHIEVEMENT_ID);
        verify(achievementService).saveProgress(any(AchievementProgress.class));
    }

    @Test
    void testHandleEvent_ShouldGiveAchievement_IfCurrentPointsMoreThanAchievementsPoints() {
        AchievementProgress achievementProgress = AchievementProgress.builder()
                .id(ACHIEVEMENT_PROGRESS_ID)
                .userId(USER_ID)
                .currentPoints(10)
                .build();
        Achievement achievement = Achievement.builder()
                .id(ACHIEVEMENT_ID)
                .title(TITLE_ACHIEVEMENT)
                .points(10)
                .build();

        when(achievementCache.get(TITLE_ACHIEVEMENT)).thenReturn(achievement);
        when(achievementService.hasAchievement(USER_ID, ACHIEVEMENT_ID)).thenReturn(false);
        when(achievementService.getProgress(USER_ID, ACHIEVEMENT_ID))
                .thenReturn(achievementProgress);

        eventHandler.handleEvent(new Object());

        verify(achievementService).createProgressIfNecessary(USER_ID, ACHIEVEMENT_ID);
        verify(achievementService).getProgress(USER_ID, ACHIEVEMENT_ID);
        verify(achievementService).giveAchievementIfNecessary(USER_ID, achievement);
        verify(achievementService).saveProgress(any(AchievementProgress.class));
    }

    @Test
    void testHandleEvent_ShouldSkipIfAchievementAlreadyExists() {
        Achievement achievement = Achievement.builder()
                .id(ACHIEVEMENT_ID)
                .title(TITLE_ACHIEVEMENT)
                .build();

        when(achievementCache.get(TITLE_ACHIEVEMENT)).thenReturn(achievement);
        when(achievementService.hasAchievement(USER_ID, ACHIEVEMENT_ID)).thenReturn(true);

        eventHandler.handleEvent(new Object());

        verify(achievementService, never()).createProgressIfNecessary(anyLong(), anyLong());
        verify(achievementService, never()).giveAchievementIfNecessary(anyLong(), any(Achievement.class));
    }

    @Test
    void testHandleEvent_ShouldThrowEventHandlingException() {
        when(achievementCache.get(TITLE_ACHIEVEMENT)).thenThrow(new EntityNotFoundException(""));

        assertThrows(EventHandlingException.class, () -> eventHandler.handleEvent(new Object()));
    }
}
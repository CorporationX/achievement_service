package faang.school.achievement.handler;

import faang.school.achievement.event.goal.GoalSetEvent;
import faang.school.achievement.eventhandler.goal.GoalEventHandler;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.achievement.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalEventHandlerTest {

    @Mock
    private AchievementService achievementService;

    @Mock
    private AchievementCache achievementCache;

    @InjectMocks
    private GoalEventHandler goalEventHandler;

    private final String achievementName = "TestAchievement";

    private GoalSetEvent event;
    private Achievement achievement;
    private AchievementProgress progress;

    @BeforeEach
    void setUp() {
        goalEventHandler = new GoalEventHandler(achievementService, achievementCache, achievementName);
        event = mock(GoalSetEvent.class);
        when(event.getUserId()).thenReturn(1L);

        achievement = mock(Achievement.class);
        when(achievement.getId()).thenReturn(1L);

        when(achievementCache.get(achievementName)).thenReturn(achievement);

        progress = mock(AchievementProgress.class);
    }

    @Test
    void testHandleWhenUserDoesNotHaveAchievementShouldCreateProgressAndGiveAchievement() {
        when(achievement.getPoints()).thenReturn(10L);
        when(progress.getCurrentPoints()).thenReturn(10L);
        when(achievementService.hasAchievement(1L, 1L)).thenReturn(false);
        when(achievementService.getProgress(1L, 1L)).thenReturn(progress);

        goalEventHandler.handle(event);

        verify(achievementService, times(1)).createProgressIfNecessary(1L, 1L);
        verify(progress, times(1)).increment();
        verify(achievementService, times(1)).updateProgress(progress);
        verify(achievementService, times(1)).giveAchievement(1L, achievement);
    }

    @Test
    void testHandleWhenUserAlreadyHasAchievementShouldDoNothing() {
        when(achievementService.hasAchievement(1L, 1L)).thenReturn(true);

        goalEventHandler.handle(event);

        verify(achievementService, never()).getProgress(anyLong(), anyLong());
        verify(achievementService, never()).updateProgress(any());
        verify(achievementService, never()).giveAchievement(anyLong(), any());
    }
}

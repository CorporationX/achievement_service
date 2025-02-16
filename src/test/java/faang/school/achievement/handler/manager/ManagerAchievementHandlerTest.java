package faang.school.achievement.handler.manager;

import faang.school.achievement.event.TeamEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.achievement.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManagerAchievementHandlerTest {

    @InjectMocks
    private ManagerAchievementHandler managerAchievementHandler;

    @Mock
    private AchievementService achievementService;

    @Mock
    private TeamEvent teamEvent;

    @Mock
    private Achievement achievement;

    @Mock
    private AchievementProgress achievementProgress;

    @BeforeEach
    void setUp() {
        when(achievementService.getAchievementByTitleWithOutUserAndProgress("MANAGER")).thenReturn(achievement);
        when(achievement.getId()).thenReturn(1L);
    }

    @Test
    void testAchievementIsGranted() {
        when(teamEvent.getAuthorId()).thenReturn(1L);
        when(achievementService.hasAchievement(any(Long.class), any(Long.class))).thenReturn(false);
        when(achievementService.getProgress(any(Long.class), any(Long.class))).thenReturn(achievementProgress);
        when(achievementProgress.getCurrentPoints()).thenReturn(100L);
        when(achievement.getPoints()).thenReturn(100L);

        managerAchievementHandler.startHandling(teamEvent);

        verify(achievementService).createNewUserAchievement(any(UserAchievement.class));
        verify(achievementProgress).increment();
        verify(achievementService).createProgressIfNecessary(any(Long.class), any(Long.class));
    }

    @Test
    void testAchievementIsNotGranted() {
        when(teamEvent.getAuthorId()).thenReturn(1L);
        when(achievementService.hasAchievement(any(Long.class), any(Long.class))).thenReturn(false);
        when(achievementService.getProgress(any(Long.class), any(Long.class))).thenReturn(achievementProgress);
        when(achievementProgress.getCurrentPoints()).thenReturn(50L);
        when(achievement.getPoints()).thenReturn(100L);

        managerAchievementHandler.startHandling(teamEvent);

        verify(achievementService, never()).createNewUserAchievement(any());
        verify(achievementProgress).increment();
        verify(achievementService).createProgressIfNecessary(any(Long.class), any(Long.class));
    }
}

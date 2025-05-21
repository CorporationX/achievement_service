package faang.school.achievement.service;

import faang.school.achievement.dto.SkillAcquiredEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementEventServiceTest {

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private AchievementEventService achievementEventService;

    long userId = 1L;
    long achievementId = 10L;
    long achievementPoints = 10L;
    SkillAcquiredEvent event;
    Achievement achievement;
    AchievementProgress progress;

    @BeforeEach
    void setUp() {
        event = mock(SkillAcquiredEvent.class);
        achievement = mock(Achievement.class);
        progress = mock(AchievementProgress.class);

        when(event.getRecipientId()).thenReturn(userId);
        when(achievement.getId()).thenReturn(achievementId);
    }

    @Test
    void testProcessAchievementAcquiredWithCreateProgressAndGiveAchievement() {

        when(achievement.getPoints()).thenReturn(achievementPoints);
        when(achievementService.hasAchievement(userId, achievementId)).thenReturn(false);
        when(achievementService.getProgress(userId, achievementId)).thenReturn(progress);
        when(progress.getCurrentPoints()).thenReturn(achievementPoints);

        achievementEventService.processAchievementAcquired(event, achievement);

        verify(achievementService).createAchievementProgressIfNecessary(userId, achievementId);
        verify(progress).increment();
        verify(achievementService).saveProgress(progress);
        verify(achievementService).giveUserAchievement(any(UserAchievement.class));
    }

    @Test
    void testProcessAchievementAcquiredIfAlreadyHasAchievement() {

        when(achievementService.hasAchievement(userId, achievementId)).thenReturn(true);

        achievementEventService.processAchievementAcquired(event, achievement);

        verify(achievementService, never()).createAchievementProgressIfNecessary(anyLong(), anyLong());
        verify(achievementService, never()).saveProgress(any());
        verify(achievementService, never()).giveUserAchievement(any());
    }

    @Test
    void testProcessAchievementAcquiredIfNotGiveAchievementWithPointsNotEnough() {

        when(achievement.getPoints()).thenReturn(achievementPoints);
        when(achievementService.hasAchievement(userId, achievementId)).thenReturn(false);
        when(achievementService.getProgress(userId, achievementId)).thenReturn(progress);
        when(progress.getCurrentPoints()).thenReturn(achievementPoints - 1);

        achievementEventService.processAchievementAcquired(event, achievement);

        verify(achievementService).createAchievementProgressIfNecessary(userId, achievementId);
        verify(progress).increment();
        verify(achievementService).saveProgress(progress);
        verify(achievementService, never()).giveUserAchievement(any());
    }
}

package faang.school.achievement.handler;

import faang.school.achievement.event.MentorshipStartEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SenseiAchievementHandlerTest {
    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private SenseiAchievementHandler achievementHandler;

    @Test
    public void testHandle_WhenUserHasAchievement_ShouldSkip() {
        Long userId = 1L;
        Long achievementId = 2L;
        Achievement achievement = new Achievement();
        achievement.setId(achievementId);
        achievement.setPoints(3);

        when(achievementService.getAchievementByTitle(anyString())).thenReturn(achievement);
        when(achievementService.hasAchievement(userId, achievementId)).thenReturn(true);

        achievementHandler.handle(new MentorshipStartEvent(userId, 10L));

        verify(achievementService, times(1)).hasAchievement(userId, achievementId);
        verifyNoMoreInteractions(achievementService);
    }

    @Test
    public void testHandle_WhenProgressUnderThreshold_ShouldUpdateProgressOnly() {
        Long userId = 1L;
        Long achievementId = 2L;
        Achievement achievement = new Achievement();
        achievement.setId(achievementId);
        achievement.setPoints(3);

        when(achievementService.getAchievementByTitle(anyString())).thenReturn(achievement);
        when(achievementService.hasAchievement(userId, achievementId)).thenReturn(false);

        AchievementProgress progress = new AchievementProgress();
        progress.setUserId(userId);
        progress.setAchievement(achievement);
        progress.setCurrentPoints(0);
        when(achievementService.createProgressIfNecessary(userId, achievementId)).thenReturn(progress);

        achievementHandler.handle(new MentorshipStartEvent(userId, 20L));

        ArgumentCaptor<AchievementProgress> argumentCaptor = ArgumentCaptor.forClass(AchievementProgress.class);
        verify(achievementService).updateProgress(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getCurrentPoints()).isEqualTo(1);
        verify(achievementService, never()).giveAchievement(anyLong(), anyLong());
    }

    @Test
    public void testHandle_WhenProgressAtThreshold_ShouldGiveAchievement() {
        Long userId = 1L;
        Long achievementId = 2L;
        Achievement achievement = new Achievement();
        achievement.setId(achievementId);
        achievement.setPoints(1);

        when(achievementService.getAchievementByTitle(anyString())).thenReturn(achievement);
        when(achievementService.hasAchievement(userId, achievementId)).thenReturn(false);

        AchievementProgress progress = new AchievementProgress();
        progress.setUserId(userId);
        progress.setAchievement(achievement);
        progress.setCurrentPoints(0);
        when(achievementService.createProgressIfNecessary(userId, achievementId)).thenReturn(progress);

        achievementHandler.handle(new MentorshipStartEvent(userId, 30L));

        verify(achievementService).giveAchievement(userId, achievementId);
    }
}

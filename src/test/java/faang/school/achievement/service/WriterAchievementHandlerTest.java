package faang.school.achievement.service;

import faang.school.achievement.dto.PostEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WriterAchievementHandlerTest {

    @Mock
    private AchievementServiceImpl achievementService;

    @InjectMocks
    private WriterAchievementHandler writerAchievementHandler;

    @Test
    void handle_WhenAchievementNotFound() {
        PostEvent event = new PostEvent();
        when(achievementService.getAchievementByTitle("Writer")).thenReturn(null);

        writerAchievementHandler.handle(event);

        verify(achievementService).getAchievementByTitle("Writer");
        verifyNoMoreInteractions(achievementService);
    }

    @Test
    void handle_WhenUserAlreadyHasAchievement() {
        PostEvent event = new PostEvent();
        Achievement writerAchievement = new Achievement();
        writerAchievement.setId(1L);
        writerAchievement.setTitle("Writer");
        writerAchievement.setPoints(10);

        when(achievementService.getAchievementByTitle("Writer")).thenReturn(writerAchievement);
        when(achievementService.hasAchievement(event.getAuthorId(), writerAchievement.getId())).thenReturn(true);

        writerAchievementHandler.handle(event);

        verify(achievementService).getAchievementByTitle("Writer");
        verify(achievementService).hasAchievement(event.getAuthorId(), writerAchievement.getId());
        verifyNoMoreInteractions(achievementService);
    }

    @Test
    void handle_WhenUserDoesNotHaveAchievement() {
        PostEvent event = new PostEvent();
        Achievement writerAchievement = new Achievement();
        writerAchievement.setId(1L);
        writerAchievement.setTitle("Writer");
        writerAchievement.setPoints(10);

        AchievementProgress progress = new AchievementProgress();
        progress.setCurrentPoints(5);
        progress.setAchievement(writerAchievement);

        when(achievementService.getAchievementByTitle("Writer")).thenReturn(writerAchievement);
        when(achievementService.hasAchievement(event.getAuthorId(), writerAchievement.getId())).thenReturn(false);
        when(achievementService.getOrCreateProgress(event.getAuthorId(), writerAchievement.getId())).thenReturn(progress);

        writerAchievementHandler.handle(event);

        verify(achievementService).getAchievementByTitle("Writer");
        verify(achievementService).hasAchievement(event.getAuthorId(), writerAchievement.getId());
        verify(achievementService).getOrCreateProgress(event.getAuthorId(), writerAchievement.getId());
        verify(achievementService).updateProgress(progress);
        assertEquals(6, progress.getCurrentPoints());
        verify(achievementService, never()).giveAchievement(anyLong(), anyLong());
    }

    @Test
    void handle_WhenProgressReachesThreshold() {
        PostEvent event = new PostEvent();
        Achievement writerAchievement = new Achievement();
        writerAchievement.setId(1L);
        writerAchievement.setTitle("Writer");
        writerAchievement.setPoints(10);

        AchievementProgress progress = new AchievementProgress();
        progress.setCurrentPoints(9);
        progress.setAchievement(writerAchievement);

        when(achievementService.getAchievementByTitle("Writer")).thenReturn(writerAchievement);
        when(achievementService.hasAchievement(event.getAuthorId(), writerAchievement.getId())).thenReturn(false);
        when(achievementService.getOrCreateProgress(event.getAuthorId(), writerAchievement.getId())).thenReturn(progress);

        writerAchievementHandler.handle(event);

        verify(achievementService).getAchievementByTitle("Writer");
        verify(achievementService).hasAchievement(event.getAuthorId(), writerAchievement.getId());
        verify(achievementService).getOrCreateProgress(event.getAuthorId(), writerAchievement.getId());
        verify(achievementService).updateProgress(progress);
        assertEquals(10, progress.getCurrentPoints());
        verify(achievementService).giveAchievement(event.getAuthorId(), writerAchievement.getId());
    }
}
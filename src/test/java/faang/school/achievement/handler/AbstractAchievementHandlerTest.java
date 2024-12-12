package faang.school.achievement.handler;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AbstractAchievementHandlerTest {

    @Mock
    private AchievementService achievementService;

    @Mock
    private AchievementCache achievementCache;

    @InjectMocks
    private TestAchievementHandler testEventHandler;

    @Test
    void handleAlreadyGivenAchievement() {
        long userId = 1L;
        long achievementId = 2L;
        String achievementTitle = "title";
        Achievement achievement = new Achievement();
        achievement.setTitle(achievementTitle);
        achievement.setId(achievementId);

        when(achievementCache.get(achievementTitle)).thenReturn(achievement);
        when(achievementService.hasAchievement(userId, achievementId)).thenReturn(true);

        assertDoesNotThrow(() -> testEventHandler.handleAchievement(userId, achievementTitle));

        verify(achievementCache, times(1)).get(achievementTitle);
        verify(achievementService, times(1)).hasAchievement(userId, achievement.getId());
        verify(achievementService, never()).createProgressIfNecessary(userId, achievement.getId());
        verify(achievementService, never()).getProgress(userId, achievement.getId());
        verify(achievementService, never()).giveAchievement(userId, achievement);
        verify(achievementService, never()).updateProgress(any());
    }

    @Test
    void handleNotFutureReceivedAchievement() {
        long userId = 1L;
        long achievementId = 2L;
        long achievementPoints = 10L;
        long currentPoints = 8L;
        String achievementTitle = "title";
        Achievement achievement = new Achievement();
        achievement.setTitle(achievementTitle);
        achievement.setId(achievementId);
        achievement.setPoints(achievementPoints);
        AchievementProgress progress = new AchievementProgress();
        progress.setCurrentPoints(currentPoints);

        when(achievementCache.get(achievementTitle)).thenReturn(achievement);
        when(achievementService.hasAchievement(userId, achievementId)).thenReturn(false);
        when(achievementService.getProgress(userId, achievementId)).thenReturn(progress);

        assertDoesNotThrow(() -> testEventHandler.handleAchievement(userId, achievementTitle));

        verify(achievementCache, times(1)).get(achievementTitle);
        verify(achievementService, times(1)).hasAchievement(userId, achievement.getId());
        verify(achievementService, times(1)).createProgressIfNecessary(userId, achievement.getId());
        verify(achievementService, times(1)).getProgress(userId, achievement.getId());
        verify(achievementService, never()).giveAchievement(userId, achievement);
        verify(achievementService, times(1)).updateProgress(any());
    }

    @Test
    void handleFutureReceivedAchievement() {
        long userId = 1L;
        long achievementId = 2L;
        long achievementPoints = 10L;
        long currentPoints = 9L;
        String achievementTitle = "title";
        Achievement achievement = new Achievement();
        achievement.setTitle(achievementTitle);
        achievement.setId(achievementId);
        achievement.setPoints(achievementPoints);
        AchievementProgress progress = new AchievementProgress();
        progress.setCurrentPoints(currentPoints);

        when(achievementCache.get(achievementTitle)).thenReturn(achievement);
        when(achievementService.hasAchievement(userId, achievementId)).thenReturn(false);
        when(achievementService.getProgress(userId, achievementId)).thenReturn(progress);

        assertDoesNotThrow(() -> testEventHandler.handleAchievement(userId, achievementTitle));

        verify(achievementCache, times(1)).get(achievementTitle);
        verify(achievementService, times(1)).hasAchievement(userId, achievement.getId());
        verify(achievementService, times(1)).createProgressIfNecessary(userId, achievement.getId());
        verify(achievementService, times(1)).getProgress(userId, achievement.getId());
        verify(achievementService, times(1)).giveAchievement(userId, achievement);
        verify(achievementService, times(1)).updateProgress(any());
    }
}

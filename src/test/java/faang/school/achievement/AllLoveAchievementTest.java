package faang.school.achievement;

import faang.school.achievement.dto.error.LikeEvent;
import faang.school.achievement.handler.like.AllLoveAchievementHandler;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AllLoveAchievementTest {

    @Mock
    AchievementService achievementService;

    @InjectMocks
    AllLoveAchievementHandler handler;

    @Test
    public void positiveGetAchievement() {
        LikeEvent event = new LikeEvent(1L, 42L, 10L);
        Achievement achievement = new Achievement();
        achievement.setId(10L);
        achievement.setTitle("ALL_LOVE");
        achievement.setPoints(1);

        AchievementProgress progress = new AchievementProgress();
        progress.setId(100L);
        progress.setCurrentPoints(0);

        when(achievementService.getAchievementByTitle("ALL_LOVE")).thenReturn(Optional.of(achievement));
        when(achievementService.hasAchievementForUser(42L, 10L)).thenReturn(false);
        when(achievementService.getAchievementProgress(42L, 10L)).thenReturn(Optional.of(progress));
        when(achievementService.incrementProgress(100L)).thenReturn(1);

        handler.handleEvent(event);

        verify(achievementService).giveAchievementForUser(42L, achievement);
    }
}

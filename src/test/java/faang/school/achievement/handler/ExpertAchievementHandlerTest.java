package faang.school.achievement.handler;

import faang.school.achievement.dto.event.CommentEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpertAchievementHandlerTest {

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    ExpertAchievementHandler expertAchievementHandler;

    private final AchievementProgress achievementProgress = new AchievementProgress();
    private final String ACHIEVEMENT_NAME = "EXPERT";
    private final Achievement achievement = new Achievement();
    private final Long userId = 1L;

    @Nested
    class Handle {
        @Test
        void achievementTrue() {
            CommentEvent event = new CommentEvent(1L, 1L, 1L, "any text");
            when(achievementService.getAchievementByName(ACHIEVEMENT_NAME)).thenReturn(achievement);
            when(achievementService.hasAchievement(userId, achievement.getId())).thenReturn(true);

            expertAchievementHandler.handle(event);

            verify(achievementService, never()).createProgressIfNecessary(userId, achievement.getId());
            verify(achievementService, never()).getProgress(userId, achievement.getId());
            verify(achievementService, never()).saveProgress(any(AchievementProgress.class));
            verify(achievementService, never()).giveAchievement(userId, achievement.getId());
        }

        @Test
        void achievementProgressIncrement() {
            achievementProgress.setCurrentPoints(0);

            CommentEvent event = new CommentEvent(1L, 1L, 1L, "any text");
            when(achievementService.getAchievementByName(ACHIEVEMENT_NAME)).thenReturn(achievement);
            when(achievementService.hasAchievement(userId, achievement.getId())).thenReturn(false);
            when(achievementService.getProgress(userId, achievement.getId())).thenReturn(achievementProgress);

            expertAchievementHandler.handle(event);

            Assertions.assertEquals(1, achievementProgress.getCurrentPoints());
            verify(achievementService, times(1)).createProgressIfNecessary(userId, achievement.getId());
            verify(achievementService, times(1)).getProgress(userId, achievement.getId());
            verify(achievementService, times(1)).saveProgress(any(AchievementProgress.class));
            verify(achievementService, never()).giveAchievement(userId, achievement.getId());
        }

        @Test
        void giveAchievement() {
            achievementProgress.setCurrentPoints(999);

            CommentEvent event = new CommentEvent(1L, 1L, 1L, "any text");
            when(achievementService.getAchievementByName(ACHIEVEMENT_NAME)).thenReturn(achievement);
            when(achievementService.hasAchievement(userId, achievement.getId())).thenReturn(false);
            when(achievementService.getProgress(userId, achievement.getId())).thenReturn(achievementProgress);

            expertAchievementHandler.handle(event);

            Assertions.assertEquals(1000, achievementProgress.getCurrentPoints());
            verify(achievementService, times(1)).createProgressIfNecessary(userId, achievement.getId());
            verify(achievementService, times(1)).getProgress(userId, achievement.getId());
            verify(achievementService, times(1)).saveProgress(any(AchievementProgress.class));
            verify(achievementService, times(1)).giveAchievement(userId, achievement.getId());
        }
    }
}

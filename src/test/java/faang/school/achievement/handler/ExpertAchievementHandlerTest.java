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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpertAchievementHandlerTest {

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    ExpertAchievementHandler expertAchievementHandler;

    private static final Long DEFAULT_ID = 1L;
    private static final String COMMENT_TEXT = "any text";
    private final String ACHIEVEMENT_NAME = "EXPERT";

    private final AchievementProgress achievementProgress = new AchievementProgress();
    private final Achievement achievement = new Achievement();

    @Nested
    class Handle {
        @Test
        void achievementTrue() {
            CommentEvent event = new CommentEvent(DEFAULT_ID, DEFAULT_ID, DEFAULT_ID, COMMENT_TEXT);
            when(achievementService.getAchievementByName(ACHIEVEMENT_NAME)).thenReturn(achievement);
            when(achievementService.hasAchievement(DEFAULT_ID, achievement.getId())).thenReturn(true);

            expertAchievementHandler.handle(event);

            verify(achievementService, never()).createProgressIfNecessary(DEFAULT_ID, achievement.getId());
            verify(achievementService, never()).getProgress(DEFAULT_ID, achievement.getId());
            verify(achievementService, never()).saveProgress(any());
            verify(achievementService, never()).giveAchievement(DEFAULT_ID, achievement.getId());
        }

        @Test
        void achievementProgressIncrement() {
            achievementProgress.setCurrentPoints(0);

            CommentEvent event = new CommentEvent(DEFAULT_ID, DEFAULT_ID, DEFAULT_ID, COMMENT_TEXT);
            when(achievementService.getAchievementByName(ACHIEVEMENT_NAME)).thenReturn(achievement);
            when(achievementService.hasAchievement(DEFAULT_ID, achievement.getId())).thenReturn(false);
            when(achievementService.getProgress(DEFAULT_ID, achievement.getId())).thenReturn(achievementProgress);

            expertAchievementHandler.handle(event);

            Assertions.assertEquals(1, achievementProgress.getCurrentPoints());
            verify(achievementService).createProgressIfNecessary(DEFAULT_ID, achievement.getId());
            verify(achievementService).getProgress(DEFAULT_ID, achievement.getId());
            verify(achievementService).saveProgress(any());
            verify(achievementService, never()).giveAchievement(DEFAULT_ID, achievement.getId());
        }

        @Test
        void giveAchievement() {
            achievementProgress.setCurrentPoints(999);

            CommentEvent event = new CommentEvent(DEFAULT_ID, DEFAULT_ID, DEFAULT_ID, COMMENT_TEXT);
            when(achievementService.getAchievementByName(ACHIEVEMENT_NAME)).thenReturn(achievement);
            when(achievementService.hasAchievement(DEFAULT_ID, achievement.getId())).thenReturn(false);
            when(achievementService.getProgress(DEFAULT_ID, achievement.getId())).thenReturn(achievementProgress);

            expertAchievementHandler.handle(event);

            Assertions.assertEquals(1000, achievementProgress.getCurrentPoints());
            verify(achievementService).createProgressIfNecessary(DEFAULT_ID, achievement.getId());
            verify(achievementService).getProgress(DEFAULT_ID, achievement.getId());
            verify(achievementService).saveProgress(any());
            verify(achievementService).giveAchievement(DEFAULT_ID, achievement.getId());
        }
    }
}

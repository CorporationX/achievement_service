package faang.school.achievement.handler.like;

import faang.school.achievement.dto.LikeEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeEventHandlerTest {
    private static final Long USER_ID = 1L;
    private static final Long ACHIEVEMENT_ID = 100L;
    private static final String TITLE = "Like Master";

    @Mock
    private AchievementServiceImpl achievementService;

    private LikeEventHandler handler;
    private LikeEvent event;
    private Achievement achievement;

    @BeforeEach
    void setUp() {
        handler = new TestLikeEventHandler(achievementService);
        event = new LikeEvent();
        event.setAuthorId(USER_ID);

        achievement = new Achievement();
        achievement.setId(ACHIEVEMENT_ID);
    }

    @Test
    void testGiveAchievement_WhenProgressReachesRequiredLikes() {
        AchievementProgress progress = new AchievementProgress();

        when(achievementService.getAchievementByTitle(TITLE)).thenReturn(achievement);
        when(achievementService.hasAchievement(USER_ID, ACHIEVEMENT_ID)).thenReturn(false);
        when(achievementService.getProgress(USER_ID, ACHIEVEMENT_ID)).thenReturn(progress);
        when(achievementService.incrementProgress(progress)).thenReturn(5L);

        handler.handle(event);

        verify(achievementService).createProgressIfNecessary(USER_ID, ACHIEVEMENT_ID);
        verify(achievementService).giveAchievement(progress);
    }

    @Test
    void testGiveAchievement_DoesNothing_WhenUserAlreadyHasAchievement() {
        when(achievementService.getAchievementByTitle(TITLE)).thenReturn(achievement);
        when(achievementService.hasAchievement(USER_ID, ACHIEVEMENT_ID)).thenReturn(true);

        handler.handle(event);

        verify(achievementService, never()).createProgressIfNecessary(anyLong(), anyLong());
        verify(achievementService, never()).giveAchievement(any());
    }

    private static class TestLikeEventHandler extends LikeEventHandler {

        public TestLikeEventHandler(AchievementServiceImpl achievementService) {
            super(achievementService);
        }

        @Override
        protected String getAchievementName() {
            return TITLE;
        }

        @Override
        protected int getRequiredLikes() {
            return 5;
        }
    }
}

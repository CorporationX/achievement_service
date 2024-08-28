package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.event.post.PostEvent;
import faang.school.achievement.messaging.handler.post.OpinionLeaderAchievementHandler;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbstractEventHandlerTest {
    @Mock
    private AchievementCache achievementCache;
    @Mock
    private AchievementService achievementService;
    @Mock
    private AchievementProgressRepository achievementProgressRepository;
    @InjectMocks
    private OpinionLeaderAchievementHandler opinionLeaderAchievementHandler;
    private String title = "OPINION LEADER";
    private Achievement achievement;
    private PostEvent postEvent;
    private AchievementProgress achievementProgress;

    @BeforeEach
    void init() {
        opinionLeaderAchievementHandler = new OpinionLeaderAchievementHandler(
                achievementCache,
                achievementService,
                title,
                achievementProgressRepository);

        postEvent = PostEvent.builder()
                .id(1L)
                .authorId(1L)
                .build();

        achievement = Achievement.builder()
                .id(1L)
                .title("OPINION LEADER")
                .description("aaa")
                .points(1000L)
                .build();

        achievementProgress = AchievementProgress.builder()
                .id(1L)
                .userId(1L)
                .currentPoints(1000L)
                .build();
    }

    @Test
    void testCreateProgressIfNecessaryException() {
        when(achievementCache.getAchievement(anyString())).thenReturn(achievement);
        when(achievementService.hasAchievement(anyLong(), anyLong())).thenReturn(false);
        doThrow(new RuntimeException("ошибка")).when(achievementService)
                .createProgressIfNecessary(anyLong(), anyLong());

        Exception exception = assertThrows(RuntimeException.class, () ->
                opinionLeaderAchievementHandler.processEvent(postEvent));
        assertEquals("ошибка", exception.getMessage());
    }

    @Test
    void testProcessEvent() throws InterruptedException {
        when(achievementCache.getAchievement(anyString())).thenReturn(achievement);
        when(achievementService.hasAchievement(anyLong(), anyLong())).thenReturn(false);
        doNothing().when(achievementService)
                .createProgressIfNecessary(anyLong(), anyLong());
        when(achievementService.getProgress(anyLong(), anyLong()))
                .thenReturn(achievementProgress);
        doNothing().when(achievementService).giveAchievement(anyLong(), any(Achievement.class));

        opinionLeaderAchievementHandler.processEvent(postEvent);

        Thread.sleep(100);

        verify(achievementService).giveAchievement(anyLong(), any(Achievement.class));
    }
}
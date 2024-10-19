package faang.school.achievement.handler;

import faang.school.achievement.dto.PostCreatedEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WriterHandlerTest {
    private static final String ACHIEVEMENT_TITLE = "WRITER";

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private WriterHandler writerHandler;

    private PostCreatedEvent event;
    private Achievement achievement;
    private AchievementProgress achievementProgress;

    @BeforeEach
    void setUp() {
        event = PostCreatedEvent.builder()
                .postId(1L)
                .authorId(2L)
                .build();
        achievement = Achievement.builder()
                .id(3L)
                .title(ACHIEVEMENT_TITLE)
                .build();
        achievementProgress = AchievementProgress.builder()
                .achievement(achievement)
                .userId(2L)
                .build();
    }

    @Test
    void testHandleEvent_Success_AchievementExists() {
        when(achievementService.findByTitle(ACHIEVEMENT_TITLE)).thenReturn(achievement);
        when(achievementService.isUserHasAchievement(2L, 3L)).thenReturn(true);

        writerHandler.handleEvent(event);

        verify(achievementService).isUserHasAchievement(anyLong(), anyLong());
        verify(achievementService, never()).createProgressIfNecessaryAndReturn(anyLong(), anyLong());
    }

    @Test
    void testHandleEvent_Success_CreateAchievementProgress() {
        when(achievementService.findByTitle(ACHIEVEMENT_TITLE)).thenReturn(achievement);
        when(achievementService.isUserHasAchievement(2L, 3L)).thenReturn(false);
        when(achievementService.createProgressIfNecessaryAndReturn(2L, 3L)).thenReturn(achievementProgress);
        doNothing().when(achievementService).incrementProgress(achievementProgress);

        writerHandler.handleEvent(event);
    }
}
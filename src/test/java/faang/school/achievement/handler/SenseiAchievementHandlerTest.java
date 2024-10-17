package faang.school.achievement.handler;

import faang.school.achievement.model.entity.Achievement;
import faang.school.achievement.model.entity.AchievementProgress;
import faang.school.achievement.model.event.MentorshipStartEvent;
import faang.school.achievement.service.impl.AchievementServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SenseiAchievementHandlerTest {

//    @Mock
//    private AchievementCache achievementCache;

    @Mock
    private AchievementServiceImpl achievementService;

    @InjectMocks
    private SenseiAchievementHandler senseiAchievementHandler;

    @Captor
    private ArgumentCaptor<AchievementProgress> progressCaptor;

    private final String senseiTitle = "SENSEI";

    @Test
    void handle_isOk() {
        // given
        MentorshipStartEvent event = MentorshipStartEvent.builder()
                .mentorId(1L)
                .menteeId(1L)
                .build();
        Achievement achievement = Achievement.builder()
                .id(4L).title("SENSEI")
                .points(20)
                .build();
        AchievementProgress achievementProgress = AchievementProgress.builder()
                .id(1L)
                .achievement(achievement)
                .currentPoints(0)
                .build();
        when(achievementService.hasAchievement(1L, 4)).thenReturn(false);
        when(achievementService.getProgress(anyLong(), anyLong())).thenReturn(achievementProgress);
        senseiAchievementHandler = new SenseiAchievementHandler(achievementService, senseiTitle);
        // when
        senseiAchievementHandler.handle(event);
        // then
        verify(achievementService).createProgress(anyLong(), anyLong());
        verify(achievementService).getProgress(anyLong(), anyLong());
        verify(achievementService, never()).giveAchievement(anyLong(), anyLong());
        verify(achievementService).saveProgress(progressCaptor.capture());
        assertEquals(1, progressCaptor.getValue().getCurrentPoints());
    }
}

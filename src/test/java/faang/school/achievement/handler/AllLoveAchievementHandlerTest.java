package faang.school.achievement.handler;

import faang.school.achievement.model.dto.AchievementRedisDto;
import faang.school.achievement.model.entity.Achievement;
import faang.school.achievement.model.entity.AchievementProgress;
import faang.school.achievement.model.event.LikeEvent;
import faang.school.achievement.model.event.PostEvent;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.impl.AchievementServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AllLoveAchievementHandlerTest {

    @Mock
    private AchievementCache achievementCache;

    @Mock
    private AchievementServiceImpl achievementService;

    @InjectMocks
    private AllLoveAchievementHandler allLoveAchievementHandler;

    @Captor
    private ArgumentCaptor<AchievementProgress> progressCaptor;

    private final String allLoveTitle = "LOVE ALL";

    @Test
    void testHandleOk() {
        LikeEvent event = LikeEvent.builder()
                .likeAuthorId(1L)
                .postId(1L)
                .postAuthorId(2L)
                .likedTime(LocalDateTime.now())
                .build();

        Achievement achievement = Achievement.builder()
                .id(9L).title("LOVE ALL")
                .points(20)
                .build();

        AchievementProgress achievementProgress = AchievementProgress.builder()
                .id(1L)
                .achievement(achievement)
                .currentPoints(0)
                .build();
        AchievementRedisDto achievementRedisDto = new AchievementRedisDto();
        achievementRedisDto.setId(9);

        when(achievementCache.getAchievementCache(anyString())).thenReturn(achievementRedisDto);
        when(achievementService.hasAchievement(1L, 9)).thenReturn(false);
        when(achievementService.getProgress(anyLong(), anyLong())).thenReturn(achievementProgress);


        allLoveAchievementHandler = new AllLoveAchievementHandler(
                achievementCache,
                achievementService,
                allLoveTitle
        );

        allLoveAchievementHandler.handle(event);

        verify(achievementService).createProgress(anyLong(), anyLong());
        verify(achievementService).getProgress(anyLong(), anyLong());
        verify(achievementService, never()).giveAchievement(anyLong(), anyLong());
        verify(achievementService).saveProgress(progressCaptor.capture());

        assertEquals(1, progressCaptor.getValue().getCurrentPoints());
    }
}

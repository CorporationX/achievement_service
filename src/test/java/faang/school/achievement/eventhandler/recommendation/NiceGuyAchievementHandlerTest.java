package faang.school.achievement.eventhandler.recommendation;

import faang.school.achievement.event.recommendation.RecommendationEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementCache;
import faang.school.achievement.service.achievement.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NiceGuyAchievementHandlerTest {

    @Mock
    private AchievementService achievementService;

    @Mock
    private AchievementCache achievementCache;

    @InjectMocks
    private NiceGuyAchievementHandler niceGuyAchievementHandler;

    private Achievement niceGuyAchievement;

    @BeforeEach
    public void setUp() {
        niceGuyAchievement = Achievement.builder()
                .id(11L)
                .title("NICE GUY")
                .points(10)
                .description("For 10 recommendation")
                .build();
    }


    @Test
    void testHandleWhenUserDoesNotHaveAchievementShouldCreateProgressAndGiveAchievement() {
        when(achievementCache.get(niceGuyAchievement.getTitle())).thenReturn(niceGuyAchievement);
        when(achievementService.hasAchievement(1L, niceGuyAchievement.getId())).thenReturn(false);
        var progress = AchievementProgress.builder()
                .id(32L)
                .userId(1L)
                .achievement(niceGuyAchievement)
                .currentPoints(0)
                .version(1)
                .build();
        when(achievementService.getProgress(1L, niceGuyAchievement.getId())).thenReturn(
                progress
        );

        niceGuyAchievementHandler.handle(
                new RecommendationEvent(
                        1L,
                        2L,
                        "Some recommendation..."
                )
        );

        verify(achievementService, times(1)).createProgressIfNecessary(1L, niceGuyAchievement.getId());
        verify(achievementService, times(1)).updateProgress(
                progress.toBuilder()
                        .currentPoints(1)
                        .build()
        );
    }

    @Test
    void testObtainingAchievement() {
        when(achievementCache.get(niceGuyAchievement.getTitle())).thenReturn(niceGuyAchievement);
        when(achievementService.hasAchievement(1L, niceGuyAchievement.getId())).thenReturn(false);

        var progress = AchievementProgress.builder()
                .id(32L)
                .userId(1L)
                .achievement(niceGuyAchievement)
                .currentPoints(niceGuyAchievement.getPoints() - 1)
                .version(1)
                .build();

        when(achievementService.getProgress(1L, niceGuyAchievement.getId())).thenReturn(
                progress
        );

        niceGuyAchievementHandler.handle(
                new RecommendationEvent(
                        1L,
                        2L,
                        "Some recommendation..."
                )
        );

        verify(achievementService, times(1)).createProgressIfNecessary(1L, niceGuyAchievement.getId());
        verify(achievementService, times(1)).updateProgress(
                progress.toBuilder()
                        .currentPoints(niceGuyAchievement.getPoints())
                        .build()
        );
        verify(achievementService, times(1)).giveAchievement(1L, niceGuyAchievement);
    }

    @Test
    void testHandleWhenUserAlreadyHasAchievementShouldDoNothing() {
        when(achievementCache.get(niceGuyAchievement.getTitle())).thenReturn(niceGuyAchievement);
        when(achievementService.hasAchievement(1L, niceGuyAchievement.getId())).thenReturn(true);

        niceGuyAchievementHandler.handle(
                new RecommendationEvent(
                        1L,
                        2L,
                        "Some recommendation..."
                )
        );

        verify(achievementService, never()).createProgressIfNecessary(anyLong(), anyLong());
        verify(achievementService, never()).getProgress(anyLong(), anyLong());
        verify(achievementService, never()).updateProgress(any());
        verify(achievementService, never()).giveAchievement(anyLong(), any());
    }

    // complex test
    @Test
    void testCompletelyChainOfAchievementProgression() {
        when(achievementCache.get(niceGuyAchievement.getTitle())).thenReturn(niceGuyAchievement);

        when(achievementService.hasAchievement(1L, niceGuyAchievement.getId())).thenReturn(false);

        var progress = AchievementProgress.builder()
                .id(32L)
                .userId(1L)
                .achievement(niceGuyAchievement)
                .currentPoints(0)
                .version(1)
                .build();

        when(achievementService.getProgress(1L, niceGuyAchievement.getId())).thenReturn(progress);

        Consumer<Integer> doProgress = (i) -> {
            progress.setCurrentPoints(i - 1);
            RecommendationEvent event = new RecommendationEvent(1L, 2L, "Recommendation " + i);
            niceGuyAchievementHandler.handle(event);

            verify(achievementService, times(i)).createProgressIfNecessary(1L, niceGuyAchievement.getId());
            verify(achievementService, times(i)).updateProgress(progress.toBuilder().currentPoints(i).build());
        };

        int i = 1;
        for (; i < niceGuyAchievement.getPoints(); i++) {
            doProgress.accept(i);
            verify(achievementService, never()).giveAchievement(anyLong(), any());
        }

        doProgress.accept(i);
        verify(achievementService, times(1)).giveAchievement(1L, niceGuyAchievement);
    }
}

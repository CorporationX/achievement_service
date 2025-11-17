package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.event.CommentEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpertAchievementHandlerTest {
    @InjectMocks
    @Spy
    private ExpertAchievementHandler expertAchievementHandler;
    @Mock
    private AchievementCache achievementCache;
    @Mock
    private AchievementService achievementService;

    private CommentEvent commentEvent;
    private Achievement achievement;
    private AchievementProgress progress;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(expertAchievementHandler, "requiredComments", 1000);
        ReflectionTestUtils.setField(expertAchievementHandler, "achievementTitle", "Expert");

        commentEvent = new CommentEvent(1L, 10L, 100L, "Great comment!");
        achievement = Achievement.builder()
                .id(1L)
                .title("Expert")
                .points(1000)
                .build();
        progress = AchievementProgress.builder()
                .userId(1L)
                .achievement(achievement)
                .currentPoints(999)
                .build();
    }

    @Test
    @DisplayName("Не должен обрабатывать события, не являющиеся CommentEvent")
    void handle_ShouldNotProcessNonCommentEvent() {
        Object otherEvent = new Object();

        expertAchievementHandler.handle(otherEvent);

        verify(achievementCache, never()).get(any());
    }

    @Test
    @DisplayName("Должен ничего не делать, если достижение не найдено в кэше")
    void handle_ShouldDoNothing_WhenAchievementNotFound() {
        when(achievementCache.get("Expert")).thenReturn(Optional.empty());

        expertAchievementHandler.handle(commentEvent);

        verify(achievementCache, times(1)).get("Expert");
        verify(achievementService, never()).hasAchievement(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Должен ничего не делать, если пользователь уже получил достижение")
    void handle_ShouldDoNothing_WhenUserAlreadyHasAchievement() {
        when(achievementCache.get("Expert")).thenReturn(Optional.of(achievement));
        when(achievementService.hasAchievement(1L, 1L)).thenReturn(true);

        expertAchievementHandler.handle(commentEvent);

        verify(achievementService, times(1)).hasAchievement(1L, 1L);
    }

    @Test
    @DisplayName("Должен увеличить прогресс, если достижение ещё не получено и цель не достигнута")
    void handle_ShouldIncreaseProgress_WhenAchievementNotReached() {
        progress.setCurrentPoints(998);
        when(achievementCache.get("Expert")).thenReturn(Optional.of(achievement));
        when(achievementService.hasAchievement(1L, 1L)).thenReturn(false);
        when(achievementService.getProgress(1L, 1L)).thenReturn(progress);

        expertAchievementHandler.handle(commentEvent);

        verify(achievementService, times(1)).createProgressIfNecessary(1L, 1L);
        verify(achievementService, times(1)).getProgress(1L, 1L);
        verify(achievementService, times(1)).updateProgress(progress);
        verify(achievementService, never()).giveAchievement(anyLong(), anyLong());

    }

    @Test
    @DisplayName("Должен выдать достижение, если прогресс достиг требуемого значения")
    void handle_ShouldGiveAchievement_WhenProgressReached() {
        when(achievementCache.get("Expert")).thenReturn(Optional.of(achievement));
        when(achievementService.hasAchievement(1L, 1L)).thenReturn(false);
        when(achievementService.getProgress(1L, 1L)).thenReturn(progress);

        expertAchievementHandler.handle(commentEvent);

        verify(achievementService, times(1)).giveAchievement(1L, 1L);
    }
}

package faang.school.achievement.handler;

import faang.school.achievement.dto.event.EventDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.EventType;
import faang.school.achievement.sender.Sender;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.thread_pool.EventThreadPools;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventHandlerTest {

    @InjectMocks
    private EventHandler eventHandler;

    @Mock
    private AchievementService achievementService;

    @Mock
    private EventThreadPools eventThreadPools;

    @Mock
    private Sender sender;

    private final long firstAchievementId = 1;
    private final long secondAchievementId = 2;
    private final long authorId = 1;
    private final EventType eventType = EventType.PUBLISHED_POST;
    private final EventDto event = EventDto.builder().authorId(authorId).eventType(eventType).build();
    private final ExecutorService threadPool = Executors.newSingleThreadExecutor();
    private final Achievement achievement = Achievement.builder().id(secondAchievementId).event(eventType).build();
    private final List<Achievement> achievements = List.of(
            Achievement.builder().id(firstAchievementId).event(eventType).build(),
            achievement
    );

    @Test
    void handleEvent_ShouldIncrementAndSaveAchievementToUser() {
        whenAllOk(true);
        assertDoesNotThrow(() -> eventHandler.handleEvent(event).join());
        verify(eventThreadPools, times(1)).getThreadPoolFor(eventType);
        verify(achievementService, times(1)).getAchievementByEventType(eventType);
        verify(achievementService, times(1)).hasUserAchievement(authorId, firstAchievementId);
        verify(achievementService, times(1)).hasUserAchievement(authorId, secondAchievementId);
        verify(achievementService, times(1)).incrementAndCheckProgress(authorId, secondAchievementId);
        verify(achievementService, times(1)).saveAchievementToUser(authorId, achievement);
        verify(sender, times(1)).send(any());
    }

    private void whenAllOk(boolean isAchieved) {
        when(eventThreadPools.getThreadPoolFor(eventType)).thenReturn(threadPool);
        when(achievementService.getAchievementByEventType(eventType)).thenReturn(achievements);
        when(achievementService.hasUserAchievement(authorId, firstAchievementId)).thenReturn(true);
        when(achievementService.incrementAndCheckProgress(authorId, secondAchievementId)).thenReturn(isAchieved);
    }

    @Test
    void handleEvent_ShouldIncrement() {
        whenAllOk(false);

        assertDoesNotThrow(() -> eventHandler.handleEvent(event).join());
        verify(eventThreadPools, times(1)).getThreadPoolFor(eventType);
        verify(achievementService, times(1)).getAchievementByEventType(eventType);
        verify(achievementService, times(1)).hasUserAchievement(authorId, firstAchievementId);
        verify(achievementService, times(1)).hasUserAchievement(authorId, secondAchievementId);
        verify(achievementService, times(1)).incrementAndCheckProgress(authorId, secondAchievementId);
        verify(achievementService, never()).saveAchievementToUser(authorId, achievement);
        verify(sender, never()).send(any());

    }

    @Test
    void handleEvent_ShouldNothingWhenUserAchievedAllAchievement() {
        when(eventThreadPools.getThreadPoolFor(eventType)).thenReturn(threadPool);
        when(achievementService.getAchievementByEventType(eventType)).thenReturn(achievements);
        when(achievementService.hasUserAchievement(authorId, firstAchievementId)).thenReturn(true);
        when(achievementService.hasUserAchievement(authorId, secondAchievementId)).thenReturn(true);

        assertDoesNotThrow(() -> eventHandler.handleEvent(event).join());
        verify(eventThreadPools, times(1)).getThreadPoolFor(eventType);
        verify(achievementService, times(1)).getAchievementByEventType(eventType);
        verify(achievementService, times(1)).hasUserAchievement(authorId, firstAchievementId);
        verify(achievementService, times(1)).hasUserAchievement(authorId, secondAchievementId);
        verify(achievementService, never()).incrementAndCheckProgress(authorId, secondAchievementId);
        verify(achievementService, never()).saveAchievementToUser(authorId, achievement);
        verify(sender, never()).send(any());
    }

    @Test
    void handleEvent_ShouldNothingWhenAchievementByEventTypeNotExists() {
        when(eventThreadPools.getThreadPoolFor(eventType)).thenReturn(threadPool);
        when(achievementService.getAchievementByEventType(eventType)).thenReturn(List.of());

        assertDoesNotThrow(() -> eventHandler.handleEvent(event).join());
        verify(eventThreadPools, times(1)).getThreadPoolFor(eventType);
        verify(achievementService, times(1)).getAchievementByEventType(eventType);
        verify(achievementService, never()).hasUserAchievement(authorId, firstAchievementId);
        verify(achievementService, never()).hasUserAchievement(authorId, secondAchievementId);
        verify(achievementService, never()).incrementAndCheckProgress(authorId, secondAchievementId);
        verify(achievementService, never()).saveAchievementToUser(authorId, achievement);
        verify(sender, never()).send(any());
    }

    @Test
    void handleEvent_ShouldExceptionWhenEventTypeIsNull() {
        when(eventThreadPools.getThreadPoolFor(eventType)).thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> eventHandler.handleEvent(event).join());
        verify(eventThreadPools, times(1)).getThreadPoolFor(eventType);
        verify(achievementService, never()).getAchievementByEventType(eventType);
        verify(achievementService, never()).hasUserAchievement(authorId, firstAchievementId);
        verify(achievementService, never()).hasUserAchievement(authorId, secondAchievementId);
        verify(achievementService, never()).incrementAndCheckProgress(authorId, secondAchievementId);
        verify(achievementService, never()).saveAchievementToUser(authorId, achievement);
        verify(sender, never()).send(any());
    }
}
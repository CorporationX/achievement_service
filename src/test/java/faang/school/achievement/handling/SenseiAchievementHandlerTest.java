package faang.school.achievement.handling;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.event.MentorshipStartEvent;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        SenseiAchievementHandler.class
})
public class SenseiAchievementHandlerTest {

    @SpyBean
    SenseiAchievementHandler handler;

    @MockBean
    AchievementService service;

    @MockBean
    AchievementCache cache;

    private final long MENTOR_ID = 10L;
    private final long MENTEE_ID = 11L;
    private final LocalDateTime TIMESTAMP = LocalDateTime.now();

    @Test
    void extractsUserId_returnMentorId() {
        MentorshipStartEvent event = new MentorshipStartEvent(MENTOR_ID, MENTEE_ID, TIMESTAMP);

        long result = handler.extractUserId(event);

        assertEquals(MENTOR_ID, result);
    }

    @Test
    void usesCorrect_achievementCode() {
        MentorshipStartEvent event = new MentorshipStartEvent(MENTOR_ID, MENTEE_ID, TIMESTAMP);

        Achievement achievement = mock(Achievement.class);
        when(achievement.getId()).thenReturn(1L);
        when(achievement.getPoints()).thenReturn(1L);

        when(cache.getOrThrow("SENSEI"))
                .thenReturn(achievement);

        when(service.hasAchievement(anyLong(), anyLong()))
                .thenReturn(false);

        AchievementProgress progress = mock(AchievementProgress.class);
        when(progress.getCurrentPoints()).thenReturn(1L);

        when(service.getProgress(anyLong(), anyLong()))
                .thenReturn(progress);

        handler.handleEvent(event);

        verify(cache, times(1)).getOrThrow("SENSEI");
    }

    @Test
    void ensureIncrement_isCalledOnlyOnce() {
        MentorshipStartEvent event = new MentorshipStartEvent(MENTOR_ID, MENTEE_ID, TIMESTAMP);

        Achievement achievement = mock(Achievement.class);
        when(achievement.getId()).thenReturn(1L);
        when(achievement.getPoints()).thenReturn(1L);

        when(cache.getOrThrow("SENSEI"))
                .thenReturn(achievement);

        when(service.hasAchievement(anyLong(), anyLong()))
                .thenReturn(false);

        AchievementProgress progress = mock(AchievementProgress.class);
        when(progress.getCurrentPoints()).thenReturn(1L);

        when(service.getProgress(anyLong(), anyLong()))
                .thenReturn(progress);

        handler.handleEvent(event);

        verify(progress, times(1)).increment();
    }
}
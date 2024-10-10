package faang.school.achievement.event.handler;

import faang.school.achievement.event.AchievementEvent;
import faang.school.achievement.event.Event;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.publisher.AchievementPublisher;
import faang.school.achievement.service.CacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventHandlerTest {

    @InjectMocks
    private TestEventHandler eventHandler;

    @Mock
    private CacheService<String> cacheService;

    @Mock
    private AchievementPublisher achievementPublisher;

    @Captor
    private ArgumentCaptor<Duration> durationCaptor;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @Captor
    private ArgumentCaptor<AchievementEvent> achievementEventCaptor;

    private final Event event = new TestEvent(LocalDateTime.of(2024, 10, 9, 10, 30));
    private final int lifeTimeMinutes = 5;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(eventHandler, "lifeTimeMinutes", lifeTimeMinutes);
    }

    @Test
    void handleEventIfNotProcessed_eventNotInCache_processesEvent() {
        Duration correctDuration = Duration.ofMinutes(lifeTimeMinutes);
        when(cacheService.exists(anyString())).thenReturn(false);

        eventHandler.handleEventIfNotProcessed(event);

        verify(cacheService).exists(anyString());
        verify(cacheService).put(anyString(), anyString(), durationCaptor.capture());
        assertEquals(correctDuration, durationCaptor.getValue());
    }

    @Test
    void handleEventIfNotProcessed_eventAlreadyProcessed_doesNotProcessAgain() {
        when(cacheService.exists(anyString())).thenReturn(true);

        eventHandler.handleEventIfNotProcessed(event);

        verify(cacheService).exists(anyString());
        verify(cacheService, never()).put(anyString(), anyString(), any(Duration.class));
    }

    @Test
    void handleEvent_generatesCorrectKeyAndCachesEvent() {
        String correctKey = "TestEventHandler:Event:2024-10-09T10:30";
        when(cacheService.exists(correctKey)).thenReturn(false);

        eventHandler.handleEventIfNotProcessed(event);

        verify(cacheService).exists(stringCaptor.capture());
        assertEquals(correctKey, stringCaptor.getValue());
    }

    @Test
    void publishNotification_PublishesCorrectEvent() {
        long userId = 1L;
        Achievement achievement = Achievement.builder()
                .id(1L)
                .title("title")
                .description("description")
                .build();

        eventHandler.publishNotification(userId, achievement);

        verify(achievementPublisher).publish(achievementEventCaptor.capture());
        AchievementEvent capturedEvent = achievementEventCaptor.getValue();

        assertEquals(userId, capturedEvent.getUserId());
        assertEquals(achievement.getId(), capturedEvent.getAchievementId());
        assertEquals(achievement.getTitle(), capturedEvent.getTitle());
        assertEquals(achievement.getDescription(), capturedEvent.getDescription());
    }

    private static class TestEventHandler extends EventHandler<Event> {
        public TestEventHandler(CacheService<String> cacheService, AchievementPublisher achievementPublisher) {
            super(cacheService, achievementPublisher);
        }

        @Override
        protected void handleEvent(Event event) {

        }

        @Override
        protected Class<Event> getEventClass() {
            return Event.class;
        }

        @Override
        protected Class<?> getHandlerClass() {
            return TestEventHandler.class;
        }
    }

    private static class TestEvent extends Event {
        public TestEvent(LocalDateTime eventTime) {
            super(eventTime);
        }
    }
}

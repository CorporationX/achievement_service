package faang.school.achievement.dto.handler;

import faang.school.achievement.dto.Event;
import faang.school.achievement.handler.AchievementEventHandler;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.achievement.AchievementService;
import faang.school.achievement.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AchievementEventHandlerTest {

    @Mock
    private CacheService<Achievement> achievementCacheService;

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private TestAchievementEventHandler achievementEventHandler;

    private Long userId;
    private Event testEvent;
    private long achievementId;
    private AchievementProgress achievementProgress;
    private Achievement achievement;

    @BeforeEach
    void setUp() {
        userId = 123L;
        testEvent = mock(Event.class);
        achievementId = 100;
        achievementProgress = AchievementProgress.builder().build();
        String achievementName = "TestAchievement";
        achievement = Achievement.builder()
                .id(achievementId)
                .title(achievementName)
                .build();
        when(achievementCacheService.get(achievementName, Achievement.class)).thenReturn(achievement);
    }

    @Test
    void handleEvent_userDoesNotHaveAchievement_createsProgressAndGivesAchievement() {
        achievement.setPoints(5L);
        achievementProgress.setCurrentPoints(10);
        when(achievementService.hasAchievement(userId, achievementId)).thenReturn(false);
        when(achievementService.getProgress(userId, achievementId)).thenReturn(achievementProgress);

        achievementEventHandler.handleEvent(testEvent);

        verify(achievementService).createProgressIfNecessary(userId, achievementId);
        verify(achievementService).giveAchievement(userId, achievement);
    }

    @Test
    void handleEvent_userAlreadyHasAchievement_doesNotCreateProgressOrGiveAchievement() {
        when(achievementService.hasAchievement(userId, achievementId)).thenReturn(true);

        achievementEventHandler.handleEvent(testEvent);

        verify(achievementService, never()).createProgressIfNecessary(anyLong(), anyLong());
        verify(achievementService, never()).giveAchievement(anyLong(), any(Achievement.class));
    }

    @Test
    void handleEvent_userHasNotReachedPoints_doesNotGiveAchievement() {
        achievement.setPoints(10L);
        achievementProgress.setCurrentPoints(5);
        when(achievementService.hasAchievement(userId, achievementId)).thenReturn(false);
        when(achievementService.getProgress(userId, achievementId)).thenReturn(achievementProgress);

        achievementEventHandler.handleEvent(testEvent);

        verify(achievementService).createProgressIfNecessary(userId, achievementId);
        verify(achievementService, never()).giveAchievement(anyLong(), any(Achievement.class));
    }

    private static class TestAchievementEventHandler extends AchievementEventHandler<Event> {

        public TestAchievementEventHandler(CacheService<String> cacheService,
                                           CacheService<Achievement> achievementCacheService,
                                           AchievementService achievementService) {
            super(cacheService, achievementCacheService, achievementService);
        }

        @Override
        protected String getAchievementName() {
            return "TestAchievement";
        }

        @Override
        protected long getUserIdFromEvent(Event event) {
            return 123L;
        }

        @Override
        protected Class<Event> getEventClass() {
            return null;
        }

        @Override
        protected Class<?> getHandlerClass() {
            return null;
        }
    }
}

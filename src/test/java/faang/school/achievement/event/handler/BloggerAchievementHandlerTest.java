package faang.school.achievement.event.handler;

import faang.school.achievement.event.AchievementEvent;
import faang.school.achievement.event.FollowerEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.publisher.AchievementPublisher;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.CacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BloggerAchievementHandlerTest {

    @Mock
    private CacheService<Achievement> achievementCacheService;

    @Mock
    private AchievementService achievementService;

    @Mock
    private AchievementPublisher achievementPublisher;

    @InjectMocks
    private BloggerAchievementHandler bloggerAchievementHandler;

    private Long userId;
    private FollowerEvent followerEvent;
    private Long achievementId;
    private AchievementProgress achievementProgress;
    private Achievement achievement;

    @BeforeEach
    void setUp() {
        userId = 123L;
        followerEvent = new FollowerEvent(LocalDateTime.now(), userId);
        achievementId = 100L;
        achievementProgress = AchievementProgress.builder()
                .build();
        String achievementName = "BLOGGER";
        achievement = Achievement.builder()
                .id(achievementId)
                .title(achievementName)
                .build();
        when(achievementCacheService.get(achievementName, Achievement.class)).thenReturn(achievement);
    }

    @Test
    void handleEvent_userDoesNotHaveAchievement_createsProgressAndGivesAchievement() {
        achievement.setPoints(5);
        achievementProgress.setCurrentPoints(10);
        when(achievementService.hasAchievement(userId, achievementId)).thenReturn(false);
        when(achievementService.getProgress(userId, achievementId)).thenReturn(achievementProgress);

        bloggerAchievementHandler.handleEvent(followerEvent);

        verify(achievementService).createProgressIfNecessary(userId, achievementId);
        verify(achievementService).giveAchievement(userId, achievement);
        verify(achievementPublisher).publish(any(AchievementEvent.class));
    }

    @Test
    void handleEvent_userAlreadyHasAchievement_doesNotCreateProgressOrGiveAchievement() {
        when(achievementService.hasAchievement(userId, achievementId)).thenReturn(true);

        bloggerAchievementHandler.handleEvent(followerEvent);

        verify(achievementService, never()).createProgressIfNecessary(anyLong(), anyLong());
        verify(achievementService, never()).giveAchievement(anyLong(), any(Achievement.class));
        verify(achievementPublisher, never()).publish(any(AchievementEvent.class));
    }

    @Test
    void handleEvent_userHasNotReachedPoints_doesNotGiveAchievement() {
        achievement.setPoints(10);
        achievementProgress.setCurrentPoints(5);
        when(achievementService.hasAchievement(userId, achievementId)).thenReturn(false);
        when(achievementService.getProgress(userId, achievementId)).thenReturn(achievementProgress);

        bloggerAchievementHandler.handleEvent(followerEvent);

        verify(achievementService).createProgressIfNecessary(userId, achievementId);
        verify(achievementService, never()).giveAchievement(anyLong(), any(Achievement.class));
    }
}

package faang.school.achievement.handler;

import faang.school.achievement.event.FollowerEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.interfaces.Cache;
import faang.school.achievement.service.interfaces.AchievementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowerEventHandlerTest {

    private static final String ACHIEVEMENT_TITLE = "100_SUBSCRIBERS";

    @Mock
    private AchievementService achievementService;

    @Mock
    private Cache<Achievement> achievementCache;

    @InjectMocks
    private FollowerEventHandler eventHandler;

    @Test
    void testHandleEventHasAchievement() {
        long followeeId = 1L;
        long achievementId = 1L;
        Achievement achievement = new Achievement();
        achievement.setId(achievementId);
        FollowerEvent followerEvent = new FollowerEvent();
        followerEvent.setFolloweeId(followeeId);
        when(achievementCache.get(ACHIEVEMENT_TITLE)).thenReturn(achievement);
        when(achievementService.hasAchievement(followeeId, achievementId)).thenReturn(true);

        eventHandler.handleEvent(followerEvent);

        verify(achievementCache).get(ACHIEVEMENT_TITLE);
        verify(achievementService).hasAchievement(followeeId, achievementId);
        verify(achievementService, times(0)).createProgressIfNecessary(followeeId, achievementId);
    }

    @Test
    void testHandleEventProgressNotEnough() {
        long followeeId = 1L;
        long achievementId = 1L;
        Achievement achievement = new Achievement();
        achievement.setId(achievementId);
        FollowerEvent followerEvent = new FollowerEvent();
        followerEvent.setFolloweeId(followeeId);
        AchievementProgress achievementProgress = new AchievementProgress();
        achievementProgress.setCurrentPoints(1);
        when(achievementCache.get(ACHIEVEMENT_TITLE)).thenReturn(achievement);
        when(achievementService.hasAchievement(followeeId, achievementId)).thenReturn(false);
        when(achievementService.getProgress(followeeId, achievementId)).thenReturn(achievementProgress);

        eventHandler.handleEvent(followerEvent);

        verify(achievementCache).get(ACHIEVEMENT_TITLE);
        verify(achievementService).hasAchievement(followeeId, achievementId);
        verify(achievementService).createProgressIfNecessary(followeeId, achievementId);
        verify(achievementService).getProgress(followeeId, achievementId);
    }


    @Test
    void testHandleEvent() {
        long followeeId = 1L;
        long achievementId = 1L;
        Achievement achievement = new Achievement();
        achievement.setId(achievementId);
        achievement.setPoints(3);
        FollowerEvent followerEvent = new FollowerEvent();
        followerEvent.setFolloweeId(1L);
        AchievementProgress achievementProgress = new AchievementProgress();
        achievementProgress.setCurrentPoints(2);
        AchievementProgress addedProgress = new AchievementProgress();
        achievementProgress.setCurrentPoints(2);
        addedProgress.increment();
        UserAchievement userAchievement = new UserAchievement();
        userAchievement.setAchievement(achievement);
        userAchievement.setUserId(followeeId);
        when(achievementCache.get(ACHIEVEMENT_TITLE)).thenReturn(achievement);
        when(achievementService.hasAchievement(followeeId, achievementId)).thenReturn(false);
        when(achievementService.getProgress(followeeId, achievementId)).thenReturn(achievementProgress);

        eventHandler.handleEvent(followerEvent);

        verify(achievementCache).get(ACHIEVEMENT_TITLE);
        verify(achievementService).hasAchievement(followeeId, achievementId);
        verify(achievementService).createProgressIfNecessary(followeeId, achievementId);
        verify(achievementService).getProgress(followeeId, achievementId);
        verify(achievementService).giveAchievement(followeeId, achievementId);
    }
}

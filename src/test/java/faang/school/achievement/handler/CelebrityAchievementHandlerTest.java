package faang.school.achievement.handler;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.user.UserDto;
import faang.school.achievement.event.follower.FollowEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static faang.school.achievement.handler.CelebrityAchievementHandler.ACHIEVEMENT_NAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CelebrityAchievementHandlerTest {
    @Mock
    private UserService userService;
    @Mock
    private AchievementService achievementService;
    @Mock
    private AchievementCache achievementCache;
    @InjectMocks
    private CelebrityAchievementHandler celebrityAchievementHandler;

    private UserDto userDto;
    private FollowEvent followEvent;
    private Achievement achievement;

    private final long followeeId = 1L;
    private final long followerId = 2L;
    private final long achievementId = 1L;
    private final long points = 2L;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(followeeId)
                .build();
        followEvent = FollowEvent.builder()
                .followeeId(followeeId)
                .followerId(followerId)
                .build();
        achievement = Achievement.builder()
                .id(achievementId)
                .points(points)
                .build();
    }

    @Test
    void testHandle_UserHasAchievement() {
        mockGetAchievementByTitle(achievement);
        mockGetUserById(userDto, followerId, followeeId);
        mockHasAchievement(true, followeeId);

        celebrityAchievementHandler.handle(followEvent);

        verify(achievementService, never()).giveAchieve(anyLong(), anyLong());
        verify(achievementService, never()).saveProgress(any(AchievementProgress.class));
        verify(achievementService, never()).createProgressIfNecessary(anyLong(), anyLong());
    }

    @Test
    void testHandle_UserWillNotGetAchievement() {
        mockGetAchievementByTitle(achievement);
        mockGetUserById(userDto, followerId, followeeId);
        mockHasAchievement(false, followeeId);
        mockGetProgress(points - 2, followeeId);

        celebrityAchievementHandler.handle(followEvent);

        verify(achievementService, never()).giveAchieve(anyLong(), anyLong());
        verify(achievementService, atLeastOnce()).createProgressIfNecessary(
                achievementId, followeeId
        );
        verify(achievementService, atLeastOnce()).saveProgress(any(AchievementProgress.class));
    }

    @Test
    void testHandle_UserWillGetAchievement() {
        mockGetAchievementByTitle(achievement);
        mockGetUserById(userDto, followerId, followeeId);
        mockHasAchievement(false, followeeId);
        mockGetProgress(points - 1, followeeId);

        celebrityAchievementHandler.handle(followEvent);

        verify(achievementService, atLeastOnce()).giveAchieve(achievementId, followeeId);
        verify(achievementService, atLeastOnce()).createProgressIfNecessary(
                achievementId, followeeId
        );
        verify(achievementService, atLeastOnce()).saveProgress(any(AchievementProgress.class));
    }

    private void mockGetProgress(long points, long followeeId) {
        when(achievementService.getProgress(followeeId, achievementId))
                .thenReturn(AchievementProgress.builder()
                        .currentPoints(points)
                        .build());
    }

    private void mockGetAchievementByTitle(Achievement achievement) {
        when(achievementCache.get(ACHIEVEMENT_NAME)).thenReturn(achievement);
    }

    private void mockGetUserById(UserDto userDto, long followerId, long followeeId) {
        when(userService.getUserById(followeeId, followerId)).thenReturn(userDto);
    }

    private void mockHasAchievement(boolean hasAchievement, long followeeId) {
        when(achievementService.hasAchievement(followeeId, achievementId)).thenReturn(hasAchievement);
    }
}

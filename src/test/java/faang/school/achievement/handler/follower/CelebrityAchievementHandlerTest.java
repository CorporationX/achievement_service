package faang.school.achievement.handler.follower;

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

import static faang.school.achievement.handler.follower.CelebrityAchievementHandler.ACHIEVEMENT_NAME;
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

        verify(achievementService, never()).giveAchievement(anyLong(), anyLong());
        verify(achievementService, never()).saveProgress(any(AchievementProgress.class));
        verify(achievementService, never()).createProgressIfNecessaryAndReturn(anyLong(), anyLong());
    }

    @Test
    void testHandle_UserWillNotGetAchievement() {
        mockGetAchievementByTitle(achievement);
        mockGetUserById(userDto, followerId, followeeId);
        mockHasAchievement(false, followeeId);
        mockCreateProgressIfNecessaryAndReturn(points - 2, followeeId);

        celebrityAchievementHandler.handle(followEvent);

        verify(achievementService, never()).giveAchievement(anyLong(), anyLong());
        verify(achievementService, atLeastOnce()).createProgressIfNecessaryAndReturn(
                achievementId, followeeId
        );
        verify(achievementService, atLeastOnce()).saveProgress(any(AchievementProgress.class));
    }

    @Test
    void testHandle_UserWillGetAchievement() {
        mockGetAchievementByTitle(achievement);
        mockGetUserById(userDto, followerId, followeeId);
        mockHasAchievement(false, followeeId);
        mockCreateProgressIfNecessaryAndReturn(points - 1, followeeId);

        celebrityAchievementHandler.handle(followEvent);

        verify(achievementService, atLeastOnce()).giveAchievement(achievementId, followeeId);
        verify(achievementService, atLeastOnce()).createProgressIfNecessaryAndReturn(
                achievementId, followeeId
        );
        verify(achievementService, atLeastOnce()).saveProgress(any(AchievementProgress.class));
    }

    private void mockCreateProgressIfNecessaryAndReturn(long points, long followeeId) {
        when(achievementService.createProgressIfNecessaryAndReturn(followeeId, achievementId))
                .thenReturn(AchievementProgress.builder()
                        .currentPoints(points)
                        .build());
    }

    private void mockGetAchievementByTitle(Achievement achievement) {
        when(achievementService.getAchievementByTitle(ACHIEVEMENT_NAME)).thenReturn(achievement);
    }

    private void mockGetUserById(UserDto userDto, long followerId, long followeeId) {
        when(userService.getUserById(followeeId, followerId)).thenReturn(userDto);
    }

    private void mockHasAchievement(boolean hasAchievement, long followeeId) {
        when(achievementService.hasAchievement(followeeId, achievementId)).thenReturn(hasAchievement);
    }
}

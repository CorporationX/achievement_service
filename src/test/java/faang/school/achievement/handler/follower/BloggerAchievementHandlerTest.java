package faang.school.achievement.handler.follower;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.FollowerEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.achievement.AchievementService;
import faang.school.achievement.service.achievementprogress.AchievementProgressService;
import faang.school.achievement.service.cache.AchievementCacheService;
import faang.school.achievement.service.userachievement.UserAchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BloggerAchievementHandlerTest {

    @Mock
    private AchievementService achievementService;

    @Mock
    private AchievementCacheService achievementCacheService;

    @Mock
    private AchievementProgressService achievementProgressService;

    @Mock
    private UserAchievementService userAchievementService;

    private BloggerAchievementHandler handler;

    private FollowerEvent event;
    private Achievement achievement;
    private AchievementDto achievementDto;
    private AchievementProgress achievementProgress;

    private static final long USER_ID = 1L;
    private static final long ACHIEVEMENT_ID = 10L;
    private static final String ACHIEVEMENT_TITLE = "BLOGGER";
    private static final long ACHIEVEMENT_POINTS = 10L;

    @BeforeEach
    public void setUp() {
        setUpHandler();
        setUpEvent();
        setUpAchievement();
        setUpAchievementDto();
        setUpAchievementProgress();
    }

    @Test
    public void shouldSkipWhenUserAlreadyHasAchievement() {
        when(achievementCacheService.getAchievement(ACHIEVEMENT_TITLE)).thenReturn(achievementDto);
        when(userAchievementService.hasAchievement(USER_ID, ACHIEVEMENT_ID)).thenReturn(true);

        handler.handleEvent(event);

        verifyNoInteractions(achievementProgressService, achievementService);
    }

    @Test
    void shouldIncrementPoints() {
        when(achievementCacheService.getAchievement(ACHIEVEMENT_TITLE)).thenReturn(achievementDto);
        when(userAchievementService.hasAchievement(USER_ID, ACHIEVEMENT_ID)).thenReturn(false);
        when(achievementProgressService.getProgress(USER_ID, ACHIEVEMENT_ID)).thenReturn(achievementProgress);

        AchievementProgress incrementedProgress = achievementProgress.builder()
                .currentPoints(1L)
                .build();

        when(achievementProgressService.progressIncrement(achievementProgress.getId()))
                .thenReturn(incrementedProgress);

        handler.handleEvent(event);

        verify(userAchievementService).hasAchievement(USER_ID, ACHIEVEMENT_ID);
        verify(achievementProgressService).createProgressIfNecessary(USER_ID, ACHIEVEMENT_ID);
        verify(achievementProgressService).getProgress(USER_ID, ACHIEVEMENT_ID);
        verify(achievementProgressService).progressIncrement(achievementProgress.getId());
        verifyNoInteractions(achievementService);
        verify(userAchievementService, never()).giveAchievement(anyLong(), any());
    }

    @Test
    void shouldGiveAchievementWhenProgressIsComplete() {
        when(achievementCacheService.getAchievement(ACHIEVEMENT_TITLE)).thenReturn(achievementDto);
        when(userAchievementService.hasAchievement(USER_ID, ACHIEVEMENT_ID)).thenReturn(false);
        when(achievementProgressService.getProgress(USER_ID, ACHIEVEMENT_ID)).thenReturn(achievementProgress);

        AchievementProgress incrementedProgress = achievementProgress.builder()
                .currentPoints(ACHIEVEMENT_POINTS)
                .build();

        when(achievementProgressService.progressIncrement(achievementProgress.getId()))
                .thenReturn(incrementedProgress);

        when(achievementService.getAchievement(ACHIEVEMENT_ID)).thenReturn(achievement);

        handler.handleEvent(event);

        verify(userAchievementService).giveAchievement(USER_ID, achievement);
    }

    private void setUpHandler() {
        handler = new BloggerAchievementHandler(
                achievementService,
                achievementCacheService,
                achievementProgressService,
                userAchievementService
        ) {
            @Override
            protected String getAchievementTitle() {
                return ACHIEVEMENT_TITLE;
            }
        };
    }

    private void setUpEvent() {
        event = FollowerEvent.builder()
                .followeeId(USER_ID)
                .build();
    }

    private void setUpAchievement() {
        achievement = Achievement.builder()
                .id(ACHIEVEMENT_ID)
                .title(ACHIEVEMENT_TITLE)
                .points(ACHIEVEMENT_POINTS)
                .build();
    }

    private void setUpAchievementDto() {
        achievementDto = AchievementDto.builder()
                .id(ACHIEVEMENT_ID)
                .title(ACHIEVEMENT_TITLE)
                .points(ACHIEVEMENT_POINTS)
                .build();
    }

    private void setUpAchievementProgress() {
        achievementProgress = AchievementProgress.builder()
                .id(ACHIEVEMENT_ID)
                .userId(USER_ID)
                .achievement(achievement)
                .currentPoints(0L)
                .version(1L)
                .build();
    }
}

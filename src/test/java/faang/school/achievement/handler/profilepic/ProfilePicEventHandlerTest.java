package faang.school.achievement.handler.profilepic;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.userprofile.ProfilePicEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.achievement.AchievementService;
import faang.school.achievement.service.achievementprogress.AchievementProgressService;
import faang.school.achievement.service.cache.AchievementCacheService;
import faang.school.achievement.service.userachievement.UserAchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test cases of ProfilePicEventHandlerTest")
public class ProfilePicEventHandlerTest {

    private static final long USER_ID = 1L;
    private static final long ACHIEVEMENT_ID = 1L;
    private static final String ACHIEVEMENT_TITLE = "TEST_ACHIEVEMENT";
    private static final long ACHIEVEMENT_POINTS = 1L;

    @Mock
    private AchievementCacheService achievementCacheService;

    @Mock
    private UserAchievementService userAchievementService;

    @Mock
    private AchievementProgressService achievementProgressService;

    @Mock
    private AchievementService achievementService;

    private ProfilePicEventHandler handler;

    private ProfilePicEvent event;
    private Achievement achievement;
    private AchievementDto achievementDto;
    private AchievementProgress achievementProgress;

    @BeforeEach
    public void setUp() {
        setUpHandler();
        setUpEvent();
        setUpAchievement();
        setUpAchievementDto();
        setUpAchievementProgress();
    }

    @Test
    @DisplayName("handleEvent - user has achievement")
    public void testHandleEventWithReceivedAchievement() {
        when(achievementCacheService.getAchievement(ACHIEVEMENT_TITLE)).thenReturn(achievementDto);
        when(userAchievementService.hasAchievement(USER_ID, ACHIEVEMENT_ID)).thenReturn(true);

        handler.handleEvent(event);

        verifyNoInteractions(achievementProgressService, achievementService);
    }

    @Test
    @DisplayName("handleEvent - increments points for achievement progress")
    public void testHandleEventIncrementsPoints() {
        long expectedCurrentPoints = 1L;
        achievement.setPoints(2L);
        achievementDto.setPoints(2L);

        when(achievementCacheService.getAchievement(ACHIEVEMENT_TITLE)).thenReturn(achievementDto);
        when(achievementProgressService.getProgress(USER_ID, ACHIEVEMENT_ID)).thenReturn(achievementProgress);

        handler.handleEvent(event);

        verify(userAchievementService, times(1)).hasAchievement(USER_ID, ACHIEVEMENT_ID);
        verify(achievementProgressService, times(1)).getProgress(USER_ID, ACHIEVEMENT_ID);
        assertEquals(expectedCurrentPoints, achievementProgress.getCurrentPoints());
        verifyNoInteractions(achievementService);
        verifyNoMoreInteractions(userAchievementService);
    }

    @Test
    @DisplayName("handleEvent - giving an achievement")
    public void TestHandleEventGivingAchievement() {
        when(achievementCacheService.getAchievement(ACHIEVEMENT_TITLE)).thenReturn(achievementDto);
        when(achievementProgressService.getProgress(USER_ID, ACHIEVEMENT_ID)).thenReturn(achievementProgress);
        when(achievementService.getAchievement(ACHIEVEMENT_ID)).thenReturn(achievement);

        handler.handleEvent(event);

        verify(userAchievementService, times(1)).giveAchievement(USER_ID, achievement);
    }

    private void setUpHandler() {
        handler = new ProfilePicEventHandler(
                achievementCacheService,
                userAchievementService,
                achievementProgressService,
                achievementService
        ) {
            @Override
            protected String getAchievementTitle() {
                return ACHIEVEMENT_TITLE;
            }
        };
    }

    private void setUpEvent() {
        event = ProfilePicEvent.builder()
                .userId(USER_ID)
                .picLink("http://example.com/pic.jpg")
                .timestamp(LocalDateTime.now())
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

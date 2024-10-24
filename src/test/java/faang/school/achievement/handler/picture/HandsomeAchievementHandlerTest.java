package faang.school.achievement.handler.picture;

import faang.school.achievement.config.achievent.AchievementConfiguration;
import faang.school.achievement.config.cache.AchievementCache;
import faang.school.achievement.dto.achievement.profile.ProfilePicEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.achievement.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class HandsomeAchievementHandlerTest {

    private static final String ACHIEVEMENT_NAME = "HANDSOME";

    private static final int ACHIEVEMENT_POINT = 1;

    private static final long USER_ID = 1L;
    private static final long ACHIEVEMENT_ID = 1L;
    private static final long ACHIEVEMENT_CURRENT_POINTS_EQUALS = 1L;
    private static final long ACHIEVEMENT_CURRENT_POINTS_NOT_EQUALS = 0L;

    @Mock
    private AchievementConfiguration achievementConfiguration;

    @Mock
    private AchievementService achievementService;

    @Mock
    private AchievementCache achievementCache;

    private Achievement achievement;
    private ProfilePicEvent profilePicEvent;
    private AchievementProgress achievementProgress;
    private HandsomeAchievementHandler handsomeAchievementHandler;
    private AchievementConfiguration.AchievementProp achievementProp;

    @BeforeEach
    void init() {
        handsomeAchievementHandler = new HandsomeAchievementHandler(
                achievementConfiguration,
                achievementService,
                achievementCache);

        profilePicEvent = new ProfilePicEvent();
        profilePicEvent.setUserId(USER_ID);

        achievementProp = new AchievementConfiguration.AchievementProp();
        achievementProp.setTitle(ACHIEVEMENT_NAME);
        achievementProp.setPointsToAchieve(ACHIEVEMENT_POINT);

        achievement = Achievement.builder()
                .id(ACHIEVEMENT_ID)
                .build();

        achievementProgress = AchievementProgress.builder().build();
    }

    @Test
    @DisplayName("When user does not have achievement and points enough then proceed achievement")
    void whenUserDoesNotHaveAchievementAndPointEnoughThenProceedAchievement() {
        achievementProgress.setCurrentPoints(ACHIEVEMENT_CURRENT_POINTS_EQUALS);

        when(achievementConfiguration.getHandsome())
                .thenReturn(achievementProp);
        when(achievementCache.getAchievement(achievementProp.getTitle()))
                .thenReturn(achievement);
        when(achievementService.hasAchievement(profilePicEvent.getUserId(), achievement.getId()))
                .thenReturn(false);
        when(achievementService.proceedAchievementProgress(profilePicEvent.getUserId(), achievement.getId()))
                .thenReturn(achievementProgress);

        handsomeAchievementHandler.handle(profilePicEvent);

        verify(achievementConfiguration)
                .getHandsome();
        verify(achievementCache)
                .getAchievement(achievementProp.getTitle());
        verify(achievementService)
                .hasAchievement(profilePicEvent.getUserId(), achievement.getId());
        verify(achievementService)
                .proceedAchievementProgress(profilePicEvent.getUserId(), achievement.getId());
        verify(achievementService)
                .giveAchievement(any(UserAchievement.class));
    }

    @Test
    @DisplayName("When user does not have achievement and points not enough then proceed achievement")
    void whenUserDoesNotHaveAchievementAndPointNotEnoughThenProceedAchievement() {
        achievementProgress.setCurrentPoints(ACHIEVEMENT_CURRENT_POINTS_NOT_EQUALS);

        when(achievementConfiguration.getHandsome())
                .thenReturn(achievementProp);
        when(achievementCache.getAchievement(achievementProp.getTitle()))
                .thenReturn(achievement);
        when(achievementService.hasAchievement(profilePicEvent.getUserId(), achievement.getId()))
                .thenReturn(false);
        when(achievementService.proceedAchievementProgress(profilePicEvent.getUserId(), achievement.getId()))
                .thenReturn(achievementProgress);

        handsomeAchievementHandler.handle(profilePicEvent);

        verify(achievementConfiguration)
                .getHandsome();
        verify(achievementCache)
                .getAchievement(achievementProp.getTitle());
        verify(achievementService)
                .hasAchievement(profilePicEvent.getUserId(), achievement.getId());
        verify(achievementService)
                .proceedAchievementProgress(profilePicEvent.getUserId(), achievement.getId());
    }

    @Test
    @DisplayName("When user already has achievement then do nothing")
    void whenUserAlreadyHasAchievementThenDoNothing() {
        when(achievementConfiguration.getHandsome())
                .thenReturn(achievementProp);
        when(achievementCache.getAchievement(achievementProp.getTitle()))
                .thenReturn(achievement);
        when(achievementService.hasAchievement(profilePicEvent.getUserId(), achievement.getId()))
                .thenReturn(true);

        handsomeAchievementHandler.handle(profilePicEvent);

        verify(achievementConfiguration)
                .getHandsome();
        verify(achievementCache)
                .getAchievement(achievementProp.getTitle());
        verify(achievementService)
                .hasAchievement(profilePicEvent.getUserId(), achievement.getId());
    }

    @Test
    @DisplayName("Should return ProfilePicEvent.class")
    void whenCallThenReturnExpectedClass() {
        assertEquals(ProfilePicEvent.class, handsomeAchievementHandler.getInstance());
    }
}
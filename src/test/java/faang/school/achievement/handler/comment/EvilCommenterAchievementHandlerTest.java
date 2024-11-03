package faang.school.achievement.handler.comment;

import faang.school.achievement.config.achievent.AchievementConfiguration;
import faang.school.achievement.dto.achievement.comment.NewCommentEventDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.RedisRepository;
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
public class EvilCommenterAchievementHandlerTest {

    private static final long AUTHOR_ID_ONE = 1L;
    private static final long ACHIEVEMENT_ID = 1L;
    private static final long ACHIEVEMENT_CURRENT_POINTS_EQUALS = 1L;
    private static final long ACHIEVEMENT_CURRENT_POINTS_NOT_EQUALS = 0L;

    private static final String ACHIEVEMENT_NAME = "EVIL COMMENTER";

    private static final int ACHIEVEMENT_POINT = 1;

    @Mock
    private AchievementConfiguration achievementConfiguration;

    @Mock
    private AchievementService achievementService;

    @Mock
    private RedisRepository redisRepository;

    private Achievement achievement;
    private NewCommentEventDto newCommentEventDto;
    private AchievementProgress achievementProgress;
    private EvilCommenterAchievementHandler evilCommenterAchievementHandler;
    private AchievementConfiguration.AchievementProp achievementProp;

    @BeforeEach
    void setUp() {
        evilCommenterAchievementHandler = new EvilCommenterAchievementHandler(
                achievementConfiguration,
                achievementService,
                redisRepository);

        achievement = Achievement.builder()
                .id(ACHIEVEMENT_ID)
                .build();

        newCommentEventDto = NewCommentEventDto.builder()
                .build();
        newCommentEventDto.setUserId(AUTHOR_ID_ONE);

        achievementProp = new AchievementConfiguration.AchievementProp();
        achievementProp.setTitle(ACHIEVEMENT_NAME);
        achievementProp.setPointsToAchieve(ACHIEVEMENT_POINT);

        achievementProgress = AchievementProgress.builder().build();
    }

    @Test
    @DisplayName("When user does not have achievement and points enough then proceed achievement")
    void whenUserDoesNotHaveAchievementAndPointEnoughThenProceedAchievement() {
        achievementProgress.setCurrentPoints(ACHIEVEMENT_CURRENT_POINTS_EQUALS);

        when(achievementConfiguration.getEvilCommenter())
                .thenReturn(achievementProp);
        when(redisRepository.getAchievement(achievementProp.getTitle()))
                .thenReturn(achievement);
        when(achievementService.hasAchievement(newCommentEventDto.getUserId(), achievement.getId()))
                .thenReturn(false);
        when(achievementService.proceedAchievementProgress(newCommentEventDto.getUserId(), achievement.getId()))
                .thenReturn(achievementProgress);

        evilCommenterAchievementHandler.handle(newCommentEventDto);

        verify(achievementConfiguration)
                .getEvilCommenter();
        verify(redisRepository)
                .getAchievement(achievementProp.getTitle());
        verify(achievementService)
                .hasAchievement(newCommentEventDto.getUserId(), achievement.getId());
        verify(achievementService)
                .proceedAchievementProgress(newCommentEventDto.getUserId(), achievement.getId());
        verify(achievementService)
                .giveAchievement(any(UserAchievement.class));
    }

    @Test
    @DisplayName("When user does not have achievement and points not enough then proceed achievement")
    void whenUserDoesNotHaveAchievementAndPointNotEnoughThenProceedAchievement() {
        achievementProgress.setCurrentPoints(ACHIEVEMENT_CURRENT_POINTS_NOT_EQUALS);

        when(achievementConfiguration.getEvilCommenter())
                .thenReturn(achievementProp);
        when(redisRepository.getAchievement(achievementProp.getTitle()))
                .thenReturn(achievement);
        when(achievementService.hasAchievement(newCommentEventDto.getUserId(), achievement.getId()))
                .thenReturn(false);
        when(achievementService.proceedAchievementProgress(newCommentEventDto.getUserId(), achievement.getId()))
                .thenReturn(achievementProgress);

        evilCommenterAchievementHandler.handle(newCommentEventDto);

        verify(achievementConfiguration)
                .getEvilCommenter();
        verify(redisRepository)
                .getAchievement(achievementProp.getTitle());
        verify(achievementService)
                .hasAchievement(newCommentEventDto.getUserId(), achievement.getId());
        verify(achievementService)
                .proceedAchievementProgress(newCommentEventDto.getUserId(), achievement.getId());
    }

    @Test
    @DisplayName("When user already has achievement then do nothing")
    void whenUserAlreadyHasAchievementThenDoNothing() {
        when(achievementConfiguration.getEvilCommenter())
                .thenReturn(achievementProp);
        when(redisRepository.getAchievement(achievementProp.getTitle()))
                .thenReturn(achievement);
        when(achievementService.hasAchievement(newCommentEventDto.getUserId(), achievement.getId()))
                .thenReturn(true);

        evilCommenterAchievementHandler.handle(newCommentEventDto);

        verify(achievementConfiguration)
                .getEvilCommenter();
        verify(redisRepository)
                .getAchievement(achievementProp.getTitle());
        verify(achievementService)
                .hasAchievement(newCommentEventDto.getUserId(), achievement.getId());
    }

    @Test
    @DisplayName("Should return NewCommentEventDto.class")
    void whenCallThenReturnExpectedClass() {
        assertEquals(NewCommentEventDto.class, evilCommenterAchievementHandler.getInstance());
    }
}

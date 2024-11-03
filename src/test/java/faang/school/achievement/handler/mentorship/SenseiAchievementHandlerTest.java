package faang.school.achievement.handler.mentorship;

import faang.school.achievement.config.achievent.AchievementConfiguration;
import faang.school.achievement.dto.achievement.mentorship.MentorshipStartEvent;
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
class SenseiAchievementHandlerTest {

    private SenseiAchievementHandler senseiAchievementHandler;

    @Mock
    private RedisRepository redisRepository;

    @Mock
    private AchievementService achievementService;

    @Mock
    private AchievementConfiguration achievementConfiguration;

    private static final String ACHIEVEMENT_NAME = "SENSEI";
    private static final long ID = 1L;
    private static final int ACHIEVEMENT_POINTS = 30;
    private Achievement achievement;
    private AchievementProgress achievementProgress;
    private MentorshipStartEvent mentorshipStartEvent;
    private AchievementConfiguration.AchievementProp achievementProp;

    @BeforeEach
    public void init() {
        senseiAchievementHandler = new SenseiAchievementHandler(
                achievementConfiguration,
                achievementService,
                redisRepository);

        achievement = Achievement.builder()
                .id(ID)
                .title(ACHIEVEMENT_NAME)
                .build();

        achievementProgress = AchievementProgress.builder().build();

        mentorshipStartEvent = MentorshipStartEvent.builder().build();
        mentorshipStartEvent.setUserId(ID);

        achievementProp = new AchievementConfiguration.AchievementProp();
        achievementProp.setPointsToAchieve(ACHIEVEMENT_POINTS);
        achievementProp.setTitle(ACHIEVEMENT_NAME);
    }

    @Test
    @DisplayName("Успешное получение достижения SENSEI")
    public void whenHandleEventThenGiveAchievement() {
        achievementProgress.setCurrentPoints(ACHIEVEMENT_POINTS);
        when(achievementConfiguration.getSensei()).thenReturn(achievementProp);
        when(redisRepository.getAchievement(achievementProp.getTitle())).thenReturn(achievement);
        when(achievementService.hasAchievement(mentorshipStartEvent.getUserId(), achievement.getId()))
                .thenReturn(false);
        when(achievementService.proceedAchievementProgress(mentorshipStartEvent.getUserId(), achievement.getId()))
                .thenReturn(achievementProgress);

        senseiAchievementHandler.handle(mentorshipStartEvent);

        verify(achievementConfiguration).getSensei();
        verify(redisRepository).getAchievement(achievementProp.getTitle());
        verify(achievementService).hasAchievement(mentorshipStartEvent.getUserId(), achievement.getId());
        verify(achievementService).proceedAchievementProgress(mentorshipStartEvent.getUserId(), achievement.getId());
        verify(achievementService).giveAchievement(any(UserAchievement.class));
    }

    @Test
    @DisplayName("Успешное увеличение прогресса без получения достижения SENSEI")
    public void whenHandleEventWithSmallProgressThenIncreaseProgress() {
        when(achievementConfiguration.getSensei()).thenReturn(achievementProp);
        when(redisRepository.getAchievement(achievementProp.getTitle())).thenReturn(achievement);
        when(achievementService.hasAchievement(mentorshipStartEvent.getUserId(), achievement.getId()))
                .thenReturn(false);
        when(achievementService.proceedAchievementProgress(mentorshipStartEvent.getUserId(), achievement.getId()))
                .thenReturn(achievementProgress);

        senseiAchievementHandler.handle(mentorshipStartEvent);

        verify(achievementConfiguration).getSensei();
        verify(redisRepository).getAchievement(achievementProp.getTitle());
        verify(achievementService).hasAchievement(mentorshipStartEvent.getUserId(), achievement.getId());
        verify(achievementService).proceedAchievementProgress(mentorshipStartEvent.getUserId(), achievement.getId());
    }

    @Test
    @DisplayName("Успешное завершение метода если у юзера уже есть достижение SENSEI")
    public void whenHandleEventWithUserHasAchievementThenSuccess() {
        when(achievementConfiguration.getSensei()).thenReturn(achievementProp);
        when(redisRepository.getAchievement(achievementProp.getTitle())).thenReturn(achievement);
        when(achievementService.hasAchievement(mentorshipStartEvent.getUserId(), achievement.getId()))
                .thenReturn(true);

        senseiAchievementHandler.handle(mentorshipStartEvent);

        verify(achievementConfiguration).getSensei();
        verify(redisRepository).getAchievement(achievementProp.getTitle());
        verify(achievementService).hasAchievement(mentorshipStartEvent.getUserId(), achievement.getId());
    }

    @Test
    @DisplayName("Успех при возврате MentorshipStartEvent.class")
    void whenCallThenReturnExpectedClass() {
        assertEquals(MentorshipStartEvent.class, senseiAchievementHandler.getInstance());
    }
}
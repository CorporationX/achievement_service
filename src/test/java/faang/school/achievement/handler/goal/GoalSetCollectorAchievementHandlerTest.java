package faang.school.achievement.handler.goal;

import faang.school.achievement.config.achievent.AchievementConfiguration;
import faang.school.achievement.dto.goal.GoalSetEventDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.RedisRepository;
import faang.school.achievement.service.achievement.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalSetCollectorAchievementHandlerTest {

    @Mock
    private AchievementConfiguration achievementConfiguration;
    @Mock
    private AchievementService achievementService;
    @Mock
    private RedisRepository redisRepository;

    @InjectMocks
    private GoalSetCollectorAchievementHandler handler;

    private GoalSetEventDto eventDto;
    private Achievement achievement;
    private AchievementConfiguration.AchievementProp achievementProp;
    private AchievementProgress progress;

    @BeforeEach
    void setUp() {
        achievementProp = new AchievementConfiguration.AchievementProp();
        achievementProp.setTitle("Collector");
        achievementProp.setPointsToAchieve(10);
        eventDto = GoalSetEventDto.builder().goalId(1L).build();
        achievement = Achievement.builder().id(1L).title("Collector").build();
        progress = AchievementProgress.builder().currentPoints(10).build();

        when(achievementConfiguration.getCollector()).thenReturn(achievementProp);
        when(redisRepository.getAchievement("Collector")).thenReturn(achievement);
    }

    @Test
    @DisplayName("Handle achievement and grant if progress meets points")
    void testHandleAchievementWithProgress() {
        when(achievementService.hasAchievement(eventDto.getUserId(), achievement.getId())).thenReturn(false);
        when(achievementService.proceedAchievementProgress(eventDto.getUserId(), achievement.getId()))
                .thenReturn(progress);

        handler.handle(eventDto);

        verify(achievementService).giveAchievement(any());
        verify(achievementService).proceedAchievementProgress(eventDto.getUserId(), achievement.getId());
    }

    @Test
    @DisplayName("Handle achievement without granting if progress does not meet points")
    void testHandleAchievementWithoutGranting() {
        progress.setCurrentPoints(5);
        when(achievementService.hasAchievement(eventDto.getUserId(), achievement.getId())).thenReturn(false);
        when(achievementService.proceedAchievementProgress(eventDto.getUserId(), achievement.getId()))
                .thenReturn(progress);

        handler.handle(eventDto);

        verify(achievementService, never()).giveAchievement(any());
    }
}

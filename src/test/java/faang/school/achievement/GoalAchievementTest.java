package faang.school.achievement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.kafka.GoalSetEvent;
import faang.school.achievement.kafka.impl.CollectorAchievementHandler;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GoalAchievementTest {

    @Mock
    private AchievementService achievementService;

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private CollectorAchievementHandler collectorAchievementHandler;

    @Test
    public void grantFor100Goals() throws JsonProcessingException {
        long userId = 1;
        long achievementId = 1;
        long goalId = 1;
        Achievement achievement = new Achievement();
        achievement.setId(achievementId);
        achievement.setTitle("achievement title");
        AchievementProgress mockedProgress = new AchievementProgress();
        mockedProgress.setId(1);
        mockedProgress.setCurrentPoints(100);
        mockedProgress.setAchievement(achievement);
        mockedProgress.setUserId(userId);

        when(achievementService.getAchievement(any())).thenReturn(achievement);
        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(false);
        when(achievementService.getProgress(userId, achievementId)).thenReturn(mockedProgress);
        when(achievementService.increaseAchievementProgress(mockedProgress, 1)).thenReturn(mockedProgress);
        GoalSetEvent goalSetEvent = new GoalSetEvent();
        goalSetEvent.setGoalId(goalId);
        goalSetEvent.setUserId(userId);
        collectorAchievementHandler.handle(objectMapper.writeValueAsString(goalSetEvent));

        verify(achievementService, times(1)).giveAchievement(eq(userId), eq(achievement));
    }

    @Test
    public void notGrantFor100Goals() throws JsonProcessingException {
        long userId = 1;
        long achievementId = 1;
        long goalId = 1;
        Achievement achievement = new Achievement();
        achievement.setId(achievementId);
        achievement.setTitle("achievement title");
        AchievementProgress mockedProgress = new AchievementProgress();
        mockedProgress.setId(1);
        mockedProgress.setCurrentPoints(98);
        mockedProgress.setAchievement(achievement);
        mockedProgress.setUserId(userId);

        when(achievementService.getAchievement(any())).thenReturn(achievement);
        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(false);
        when(achievementService.getProgress(userId, achievementId)).thenReturn(mockedProgress);
        when(achievementService.increaseAchievementProgress(mockedProgress, 1)).thenReturn(mockedProgress);
        GoalSetEvent goalSetEvent = new GoalSetEvent();
        goalSetEvent.setGoalId(goalId);
        goalSetEvent.setUserId(userId);
        collectorAchievementHandler.handle(objectMapper.writeValueAsString(goalSetEvent));

        verify(achievementService, never()).giveAchievement(eq(userId), eq(achievement));
    }
}

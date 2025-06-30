package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceTest {
    @Mock
    private UserAchievementRepository userAchievementRepository;
    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @InjectMocks
    private AchievementService service;

    @Test
    public void hasAchievementTest() {
        long userId = 1;
        long achievementId = 1;
        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(true);
        assertTrue(service.hasAchievement(userId, achievementId));
    }

    @Test
    public void createProgressIfNecessaryTest() {
        long userId = 1;
        long achievementId = 1;
        service.createProgressIfNecessary(userId, achievementId);
        verify(achievementProgressRepository, times(1)).createProgressIfNecessary(userId, achievementId);
    }

    @Test
    public void getProgressTest() {
        long userId = 1;
        long achievementId = 1;
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.of(new AchievementProgress()));
        assertEquals(new AchievementProgress(), service.getProgress(userId, achievementId));
    }

    @Test
    public void giveAchievement() {
        long userId = 1;
        Achievement achievement = new Achievement();
        UserAchievement newAchievement = UserAchievement.builder().userId(userId).achievement(achievement).build();
        service.giveAchievement(userId, achievement);
        verify(userAchievementRepository, times(1)).save(newAchievement);

    }
}

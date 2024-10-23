package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.achievement.AchievementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceImplTest {
    @Mock
    private AchievementProgressRepository achievementProgressRepository;
    @Mock
    private UserAchievementRepository userAchievementRepository;
    @InjectMocks
    private AchievementServiceImpl achievementService;
    private Achievement achievement;
    private UserAchievement userAchievement;
    private Long userId;
    private Long achievementId;
    @BeforeEach
    void setUp() {
        userId = 1L;
        achievementId = 2L;
        achievement = new Achievement();
        userAchievement = new UserAchievement();
        achievement.setId(achievementId);
        userAchievement.setAchievement(achievement);
        userAchievement.setUserId(userId);
    }
    @Test
    void getProgressTest() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.of(new AchievementProgress()));

        achievementService.getProgress(userId, achievementId);

        verify(achievementProgressRepository,times(1)).findByUserIdAndAchievementId(userId, achievementId);
    }
    @Test
    void giveAchievementTest() {
        when(userAchievementRepository.save(any(UserAchievement.class))).thenReturn(userAchievement);

        achievementService.giveAchievement(userId, achievement);

        verify( userAchievementRepository,times(1)).save(userAchievement);
    }
}

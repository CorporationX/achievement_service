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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceTest {

    @InjectMocks
    AchievementServiceImpl achievementService;

    @Mock
    AchievementProgressRepository achievementProgressRepository;

    @Mock
    UserAchievementRepository userAchievementRepository;

    @Test
    void testHasAchievement() {
        achievementService.hasAchievement(1L, 1L);
        verify(userAchievementRepository).existsByUserIdAndAchievementId(1L, 1L);
    }

    @Test
    void testGetProgress() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(1L, 1L)).thenReturn(Optional.of(new AchievementProgress()));
        achievementService.getProgress(1L, 1L);
        verify(achievementProgressRepository).createProgressIfNecessary(1L, 1L);
        verify(achievementProgressRepository).findByUserIdAndAchievementId(1L, 1L);
    }

    @Test
    public void testGiveAchievement() {
        Achievement achievement = new Achievement();

        List<UserAchievement> userAchievements = new ArrayList<>();
        when(userAchievementRepository.findByUserId(1L)).thenReturn(userAchievements);

        achievementService.giveAchievement(achievement, 1L);

        verify(userAchievementRepository).findByUserId(1L);
        verify(userAchievementRepository).save(any(UserAchievement.class));

        assert userAchievements.size() == 1;
        assert userAchievements.get(0).getAchievement() == achievement;
        assert userAchievements.get(0).getUserId() == 1L;
    }


}

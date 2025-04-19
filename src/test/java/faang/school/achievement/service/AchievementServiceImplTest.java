package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementServiceImplTest {

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @Mock
    private AchievementRepository achievementRepository;

    @InjectMocks
    private AchievementServiceImpl achievementService;

    @Test
    void hasAchievement_WhenExists() {
        Long userId = 1L;
        Long achievementId = 1L;
        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(true);

        boolean result = achievementService.hasAchievement(userId, achievementId);

        assertTrue(result);
        verify(userAchievementRepository).existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    void hasAchievement_WhenNotExists_ReturnsFalse() {
        Long userId = 1L;
        Long achievementId = 1L;
        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(false);

        boolean result = achievementService.hasAchievement(userId, achievementId);

        assertFalse(result);
        verify(userAchievementRepository).existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    void getOrCreateProgress_WhenProgressExists() {
        Long userId = 1L;
        Long achievementId = 1L;
        AchievementProgress existingProgress = new AchievementProgress();
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.of(existingProgress));

        AchievementProgress result = achievementService.getOrCreateProgress(userId, achievementId);

        assertEquals(existingProgress, result);
        verify(achievementProgressRepository).findByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    void getOrCreateProgress_WhenProgressNotExists() {
        Long userId = 1L;
        Long achievementId = 1L;
        Achievement achievement = new Achievement();
        achievement.setId(achievementId);

        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.empty());
        when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(achievement));

        AchievementProgress result = achievementService.getOrCreateProgress(userId, achievementId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(achievement, result.getAchievement());
        assertEquals(0, result.getCurrentPoints());
        verify(achievementProgressRepository).save(result);
    }

    @Test
    void giveAchievement_SavesNewUserAchievement() {
        Long userId = 1L;
        Long achievementId = 1L;
        Achievement achievement = new Achievement();
        achievement.setId(achievementId);

        when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(achievement));

        achievementService.giveAchievement(userId, achievementId);

        verify(achievementRepository).findById(achievementId);
        verify(userAchievementRepository).save(any(UserAchievement.class));
    }

    @Test
    void updateProgress_SavesProgress() {
        AchievementProgress progress = new AchievementProgress();

        achievementService.updateProgress(progress);

        verify(achievementProgressRepository).save(progress);
    }

    @Test
    void getAchievementByTitle_ReturnsAchievement() {
        String title = "Test Achievement";
        Achievement expectedAchievement = new Achievement();
        when(achievementRepository.findAchievementByTitle(title)).thenReturn(Optional.of(expectedAchievement));

        Achievement result = achievementService.getAchievementByTitle(title);

        assertEquals(expectedAchievement, result);
        verify(achievementRepository).findAchievementByTitle(title);
    }
}
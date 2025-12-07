package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {

    @Mock
    private AchievementRepository achievementRepository;
    @Mock
    private UserAchievementRepository userAchievementRepository;
    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @InjectMocks
    private AchievementService achievementService;

    @Test
    @DisplayName("Should return false if user does not have achievement")
    void hasAchievementFalse() {
        long userId = 1L;
        Achievement achievement = new Achievement();
        achievement.setId(10L);

        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, 10L)).thenReturn(false);

        assertFalse(achievementService.hasAchievement(userId, achievement));
    }

    @Test
    @DisplayName("Should throw exception if achievement not found by title")
    void getAchievementByTitleNotFoundThrowsException() {
        when(achievementRepository.findByTitle("UNKNOWN")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> achievementService.getAchievementByTitle("UNKNOWN"));
    }

    @Test
    @DisplayName("Should create progress and return it")
    void createProgressIfNecessarySuccess() {
        long userId = 1L;
        Achievement achievement = new Achievement();
        achievement.setId(5L);
        AchievementProgress progress = new AchievementProgress();

        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, 5L)).thenReturn(Optional.of(progress));

        AchievementProgress result = achievementService.createProgressIfNecessary(userId, achievement);

        verify(achievementProgressRepository).createProgressIfNecessary(userId, 5L);
        verify(achievementProgressRepository).findByUserIdAndAchievementId(userId, 5L);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should save progress via repository")
    void saveProgressTest() {
        AchievementProgress progress = new AchievementProgress();
        achievementService.saveProgress(progress);
        verify(achievementProgressRepository).save(progress);
    }

    @Test
    @DisplayName("Should save user achievement")
    void giveAchievementTest() {
        long userId = 1L;
        Achievement achievement = new Achievement();
        achievementService.giveAchievement(userId, achievement);
        verify(userAchievementRepository).save(any(UserAchievement.class));
    }
}
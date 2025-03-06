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

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @InjectMocks
    private AchievementService achievementService;

    @Test
    void hasAchievement_ShouldReturnTrueIfAchievementExists() {
        long userId = 1L;
        long achievementId = 1L;

        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(true);

        assertTrue(achievementService.hasAchievement(userId, achievementId));
    }

    @Test
    void hasAchievement_ShouldReturnFalseIfAchievementDoesNotExist() {
        long userId = 1L;
        long achievementId = 1L;

        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(false);

        assertFalse(achievementService.hasAchievement(userId, achievementId));
    }

    @Test
    void createProgressIfNecessary_ShouldCallRepository() {
        long userId = 1L;
        long achievementId = 1L;

        achievementService.createProgressIfNecessary(userId, achievementId);

        verify(achievementProgressRepository).createProgressIfNecessary(userId, achievementId);
    }

    @Test
    void getProgress_ShouldReturnProgressIfExists() {
        long userId = 1L;
        long achievementId = 1L;
        AchievementProgress progress = new AchievementProgress();

        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.of(progress));

        assertEquals(progress, achievementService.getProgress(userId, achievementId));
    }

    @Test
    void getProgress_ShouldThrowExceptionIfProgressDoesNotExist() {
        long userId = 1L;
        long achievementId = 1L;

        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> achievementService.getProgress(userId, achievementId));
    }

    @Test
    void giveAchievement_ShouldSaveAndReturnUserAchievement() {
        UserAchievement userAchievement = new UserAchievement();
        when(userAchievementRepository.save(userAchievement)).thenReturn(userAchievement);

        UserAchievement result = achievementService.giveAchievement(userAchievement);

        assertEquals(userAchievement, result);
        verify(userAchievementRepository).save(userAchievement);
    }

    @Test
    void processAchievement_ShouldGiveAchievementIfProgressIsComplete() {
        long userId = 1L;
        Achievement achievement = new Achievement();
        achievement.setId(1L);
        achievement.setTitle("Test Achievement");
        achievement.setPoints(10);

        AchievementProgress progress = new AchievementProgress();
        progress.setCurrentPoints(9);

        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievement.getId()))
                .thenReturn(false);
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievement.getId()))
                .thenReturn(Optional.of(progress));
        when(userAchievementRepository.save(any(UserAchievement.class)))
                .thenReturn(new UserAchievement());

        achievementService.processAchievement(achievement, userId);

        verify(achievementProgressRepository).createProgressIfNecessary(userId, achievement.getId());
        verify(userAchievementRepository).save(any(UserAchievement.class));
    }

    @Test
    void processAchievement_ShouldNotGiveAchievementIfAlreadyExists() {
        long userId = 1L;
        Achievement achievement = new Achievement();
        achievement.setId(1L);
        achievement.setTitle("Test Achievement");

        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievement.getId()))
                .thenReturn(true);

        achievementService.processAchievement(achievement, userId);

        verify(userAchievementRepository, never()).save(any(UserAchievement.class));
    }
}
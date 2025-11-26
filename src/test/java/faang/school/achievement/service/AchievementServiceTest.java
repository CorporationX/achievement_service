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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceTest {

    @Mock
    private AchievementRepository achievementRepository;
    @Mock
    private AchievementProgressRepository achievementProgressRepository;
    @Mock
    private UserAchievementRepository userAchievementRepository;
    @InjectMocks
    private AchievementService achievementService;

    private final Long USER_ID = 1L;
    private final Long ACHIEVEMENT_ID = 100L;


    @Test
    public void testHasAchievementReturnsTrue() {
        UserAchievement userAchievement = createTestUserAchievement();
        List<UserAchievement> userAchievements = List.of(userAchievement);

        when(userAchievementRepository.findByUserId(USER_ID)).thenReturn(userAchievements);

        boolean result = achievementService.hasAchievement(USER_ID, ACHIEVEMENT_ID);

        assertTrue(result);
        verify(userAchievementRepository).findByUserId(USER_ID);
    }

    @Test
    public void testHasAchievementReturnsFalse() {
        List<UserAchievement> userAchievements = List.of(createTestUserAchievement());
        when(userAchievementRepository.findByUserId(USER_ID)).thenReturn(userAchievements);

        boolean result = achievementService.hasAchievement(USER_ID, 999L); // Другой ID

        assertFalse(result);
        verify(userAchievementRepository).findByUserId(USER_ID);
    }

    @Test
    public void testCreateProgressIfNecessary() {
        doNothing().when(achievementProgressRepository)
                .createProgressIfNecessary(USER_ID, ACHIEVEMENT_ID);

        achievementService.createProgressIfNecessary(USER_ID, ACHIEVEMENT_ID);

        verify(achievementProgressRepository).createProgressIfNecessary(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    public void testGetProgress() {
        AchievementProgress progress = createTestProgress();
        when(achievementProgressRepository.findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID))
                .thenReturn(Optional.of(progress));

        long result = achievementService.getProgress(USER_ID, ACHIEVEMENT_ID);

        assertEquals(5L, result);
        verify(achievementProgressRepository).findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    public void testGetProgressZero() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID))
                .thenReturn(Optional.empty());

        long result = achievementService.getProgress(USER_ID, ACHIEVEMENT_ID);

        assertEquals(0L, result);
        verify(achievementProgressRepository).findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    public void testGiveAchievement() {
        Achievement achievement = createTestAchievement();
        UserAchievement savedUserAchievement = createTestUserAchievement();

        when(userAchievementRepository.save(any(UserAchievement.class)))
                .thenReturn(savedUserAchievement);

        achievementService.giveAchievement(USER_ID, achievement);

        verify(userAchievementRepository).save(argThat(ua ->
                USER_ID.equals(ua.getUserId()) &&
                        ua.getAchievement().equals(achievement)
        ));
    }

    @Test
    public void testGetAchievementByTitleNotFound() {
        when(achievementRepository.findByTitle("test"))
                .thenReturn(null);

        Achievement result = achievementService.getAchievementByTitle("test");

        assertNull(result);
        verify(achievementRepository).findByTitle("test");
    }

    @Test
    public void testUpdateProgress() {
        // Arrange
        long currentProgress = 7L;
        doNothing().when(achievementProgressRepository)
                .updateCurrentPoints(USER_ID, ACHIEVEMENT_ID, currentProgress);

        achievementService.updateProgress(USER_ID, ACHIEVEMENT_ID, currentProgress);

        verify(achievementProgressRepository).updateCurrentPoints(USER_ID, ACHIEVEMENT_ID, currentProgress);
    }

    private Achievement createTestAchievement() {
        return Achievement.builder()
                .id(ACHIEVEMENT_ID)
                .title("ACHIEVEMENT_TITLE")
                .points(10L)
                .build();
    }

    private UserAchievement createTestUserAchievement() {
        return UserAchievement.builder()
                .id(1L)
                .userId(USER_ID)
                .achievement(createTestAchievement())
                .build();
    }

    private AchievementProgress createTestProgress() {
        return AchievementProgress.builder()
                .id(1L)
                .userId(USER_ID)
                .achievement(createTestAchievement())
                .currentPoints(5L)
                .build();
    }
}

package faang.school.achievement.service;

import faang.school.achievement.exception.AchievementNotFoundException;
import faang.school.achievement.message.ErrorMessage;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementServiceImplTest {

    @InjectMocks
    private AchievementServiceImpl achievementService;

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @Mock
    private AchievementRepository achievementRepository;

    @Captor
    ArgumentCaptor<UserAchievement> userAchievementArgumentCaptor;

    private long userId;
    private long achievementId;
    private Achievement achievement;
    private AchievementProgress achievementProgress;
    private String title;

    @BeforeEach
    void setUp() {
        userId = 1L;
        achievementId = 2L;
        title = "TEST";

        achievement = new Achievement();
        achievement.setId(achievementId);
        achievement.setTitle(title);

        achievementProgress = new AchievementProgress();
        achievementProgress.setId(4L);
        achievementProgress.setUserId(userId);
        achievementProgress.setAchievement(achievement);
    }

    @Test
    void hasAchievement() {
        achievementService.hasAchievement(userId, achievementId);

        verify(userAchievementRepository, times(1)).existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    void createProgressIfNecessary() {
        achievementService.createProgressIfNecessary(userId, achievementId);

        verify(achievementProgressRepository, times(1)).createProgressIfNecessary(userId, achievementId);
    }

    @Test
    void getProgress() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)).thenReturn(Optional.ofNullable(achievementProgress));

        achievementService.getProgress(userId, achievementId);

        verify(achievementProgressRepository, times(1)).findByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    void giveAchievement() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(userId,achievementId)).thenReturn(false);

        achievementService.giveAchievement(achievementProgress);

        verify(userAchievementRepository,times(1)).save(userAchievementArgumentCaptor.capture());
        UserAchievement userAchievement = userAchievementArgumentCaptor.getValue();
        assertEquals(achievement, userAchievement.getAchievement());
        assertEquals(userId, userAchievement.getUserId());
    }

    @Test
    void getAchievementByTitle() {
        when(achievementRepository.findByTitle(title)).thenReturn(Optional.ofNullable(achievement));

        Achievement result = achievementService.getAchievementFindByTitle(title);

        verify(achievementRepository, times(1)).findByTitle(title);
        assertEquals(achievement, result);
    }

    @Test
    void incrementProgress() {
        long progressId = 5L;
        AchievementProgress updateProgress = new AchievementProgress();
        updateProgress.setId(progressId);
        updateProgress.setCurrentPoints(10L);
        when(achievementProgressRepository.findById(progressId)).thenReturn(Optional.of(updateProgress));

        long result = achievementService.incrementProgress(updateProgress);

        verify(achievementProgressRepository, times(1)).findById(progressId);
        verify(achievementProgressRepository, times(1)).incrementPoints(progressId);
        assertEquals(updateProgress.getCurrentPoints(), result);
    }

    @Test
    void testGetProgress_ThrowsException_WhenProgressNotFound() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                achievementService.getProgress(userId, achievementId));
        assertEquals(
                String.format(ErrorMessage.ACHIEVEMENT_PROGRESS_NOT_FOUND_BY_ID_AND_ACHIEVEMENT_ID.getMessage(),
                        userId, achievementId),
                exception.getMessage());
    }

    @Test
    void testGetAchievementByTitle_ThrowsException_WhenAchievementNotFound() {
        String title = "Super Coder";

        when(achievementRepository.findByTitle(title)).thenReturn(Optional.empty());

        AchievementNotFoundException exception = assertThrows(AchievementNotFoundException.class, () ->
                achievementService.getAchievementFindByTitle(title));
        assertEquals(
                String.format(ErrorMessage.ACHIEVEMENT_NOT_FOUND_BY_TITLE.getMessage(),title),
                exception.getMessage()
        );
    }

    @Test
    void testIncrementProgress_ThrowsException_WhenProgressNotFoundAfterIncrement() {
        AchievementProgress progress = new AchievementProgress();
        progress.setId(100L);
        when(achievementProgressRepository.findById(progress.getId())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                achievementService.incrementProgress(progress));
        assertEquals(
                String.format(ErrorMessage.ACHIEVEMENT_PROGRESS_NOT_FOUND_BY_ID.getMessage(), progress.getId()),
                exception.getMessage()
        );
    }

    @Test
    void testGiveAchievement_DoesNothing_WhenUserAlreadyHasAchievement() {
        AchievementProgress achievementProgress = mock(AchievementProgress.class);
        Achievement achievement = mock(Achievement.class);

        when(achievementProgress.getUserId()).thenReturn(userId);
        when(achievementProgress.getAchievement()).thenReturn(achievement);
        when(achievement.getId()).thenReturn(10L);
        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, 10L)).thenReturn(true);

        achievementService.giveAchievement(achievementProgress);

        verify(userAchievementRepository, never()).save(any(UserAchievement.class));
    }



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
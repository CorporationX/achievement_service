package faang.school.achievement;

import faang.school.achievement.dto.ProgressCreationResultDto;
import faang.school.achievement.dto.ProgressUpdateDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.exception.AchievementNotFoundException;
import faang.school.achievement.exception.ProgressNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

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

    private final long USER_ID = 1L;
    private final long ACHIEVEMENT_ID = 1L;
    private final String ACHIEVEMENT_TITLE = "Test Achievement";
    private Achievement achievement;
    private AchievementProgress progress;
    private UserAchievement userAchievement;

    @BeforeEach
    void setUp() {
        achievement = Achievement.builder()
                .id(ACHIEVEMENT_ID)
                .title(ACHIEVEMENT_TITLE)
                .build();

        progress = AchievementProgress.builder()
                .id(1L)
                .userId(USER_ID)
                .achievement(achievement)
                .currentPoints(0)
                .build();

        userAchievement = UserAchievement.builder()
                .id(1L)
                .userId(USER_ID)
                .achievement(achievement)
                .build();
    }

    @Test
    void testGetAchievementSuccess() {
        when(achievementRepository.findByTitle(ACHIEVEMENT_TITLE)).thenReturn(Optional.of(achievement));

        Achievement result = achievementService.getAchievement(ACHIEVEMENT_TITLE);

        assertNotNull(result);
        assertEquals(ACHIEVEMENT_ID, result.getId());
        assertEquals(ACHIEVEMENT_TITLE, result.getTitle());
        verify(achievementRepository).findByTitle(ACHIEVEMENT_TITLE);
    }

    @Test
    void testGetAchievementNotFound() {
        when(achievementRepository.findByTitle(ACHIEVEMENT_TITLE)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> achievementService.getAchievement(ACHIEVEMENT_TITLE));
        verify(achievementRepository).findByTitle(ACHIEVEMENT_TITLE);
    }

    @Test
    void testHasAchievementTrue() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID)).thenReturn(true);

        boolean result = achievementService.hasUserAchievement(USER_ID, ACHIEVEMENT_ID);

        assertTrue(result);
        verify(userAchievementRepository).existsByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    void testHasAchievementFalse() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID)).thenReturn(false);

        boolean result = achievementService.hasUserAchievement(USER_ID, ACHIEVEMENT_ID);

        assertFalse(result);
        verify(userAchievementRepository).existsByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    void testCreateProgressIfNecessaryCreated() {
        when(achievementProgressRepository.createProgressIfNecessary(USER_ID, ACHIEVEMENT_ID)).thenReturn(1);

        ProgressCreationResultDto result = achievementService.createProgressIfNecessary(USER_ID, ACHIEVEMENT_ID);

        assertNotNull(result);
        assertEquals(USER_ID, result.userId());
        assertEquals(ACHIEVEMENT_ID, result.achievementId());
        assertTrue(result.created());
        verify(achievementProgressRepository).createProgressIfNecessary(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    void testCreateProgressIfNecessaryNotCreated() {
        when(achievementProgressRepository.createProgressIfNecessary(USER_ID, ACHIEVEMENT_ID)).thenReturn(0);

        ProgressCreationResultDto result = achievementService.createProgressIfNecessary(USER_ID, ACHIEVEMENT_ID);

        assertNotNull(result);
        assertEquals(USER_ID, result.userId());
        assertEquals(ACHIEVEMENT_ID, result.achievementId());
        assertFalse(result.created());
        verify(achievementProgressRepository).createProgressIfNecessary(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    void testGetProgressSuccess() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID))
                .thenReturn(Optional.of(progress));

        AchievementProgress result = achievementService.getProgress(USER_ID, ACHIEVEMENT_ID);

        assertNotNull(result);
        assertEquals(USER_ID, result.getUserId());
        assertEquals(ACHIEVEMENT_ID, result.getAchievement().getId());
        verify(achievementProgressRepository).findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    void testGetProgressNotFound() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> achievementService.getProgress(USER_ID, ACHIEVEMENT_ID));
        verify(achievementProgressRepository).findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    void testIncrementProgressSuccess() {
        int initialPoints = Math.toIntExact(progress.getCurrentPoints());

        when(achievementProgressRepository.save(progress)).thenReturn(progress);

        ProgressUpdateDto result = achievementService.incrementProgress(progress);

        assertNotNull(result);
        assertEquals(progress.getId(), result.progressId());
        assertEquals(initialPoints + 1, result.currentPoints());
        verify(achievementProgressRepository).save(progress);
    }

    @Test
    void testIncrementProgressNullProgress() {
        assertThrows(ProgressNotFoundException.class, () -> achievementService.incrementProgress(null));
        verifyNoInteractions(achievementProgressRepository);
    }

    @Test
    void testGiveAchievementNewAchievement() {
        when(userAchievementRepository.findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID))
                .thenReturn(Optional.empty());
        when(achievementRepository.findById(ACHIEVEMENT_ID)).thenReturn(Optional.of(achievement));
        when(userAchievementRepository.save(any(UserAchievement.class))).thenReturn(userAchievement);

        UserAchievementDto result = achievementService.giveAchievement(USER_ID, ACHIEVEMENT_ID);

        assertNotNull(result);
        assertEquals(USER_ID, result.userId());
        assertEquals(ACHIEVEMENT_ID, result.achievementId());
        verify(userAchievementRepository).findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID);
        verify(achievementRepository).findById(ACHIEVEMENT_ID);
        verify(userAchievementRepository).save(any(UserAchievement.class));
    }

    @Test
    void testGiveAchievementExistingAchievement() {
        when(userAchievementRepository.findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID))
                .thenReturn(Optional.of(userAchievement));

        UserAchievementDto result = achievementService.giveAchievement(USER_ID, ACHIEVEMENT_ID);

        assertNotNull(result);
        assertEquals(USER_ID, result.userId());
        assertEquals(ACHIEVEMENT_ID, result.achievementId());
        verify(userAchievementRepository).findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID);
        verify(achievementRepository, never()).findById(anyLong());
        verify(userAchievementRepository, never()).save(any());
    }

    @Test
    void testGiveAchievementAchievementNotFound() {
        when(userAchievementRepository.findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID))
                .thenReturn(Optional.empty());
        when(achievementRepository.findById(ACHIEVEMENT_ID)).thenReturn(Optional.empty());

        assertThrows(AchievementNotFoundException.class, () -> achievementService.giveAchievement(USER_ID, ACHIEVEMENT_ID));
        verify(userAchievementRepository).findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID);
        verify(achievementRepository).findById(ACHIEVEMENT_ID);
        verify(userAchievementRepository, never()).save(any());
    }
}
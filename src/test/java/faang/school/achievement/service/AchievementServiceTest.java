package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.MessagePublisher;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceTest {
    private final static Long USER_ID = 1L;
    private final static Long ACHIEVEMENT_ID = 1L;

    private Achievement achievement;
    private UserAchievement userAchievement;
    private AchievementProgress achievementProgress;
    private AchievementEvent achievementEvent;

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private MessagePublisher<AchievementEvent> achievementPublisher;

    @InjectMocks
    private AchievementServiceImpl achievementService;

    @BeforeEach
    public void setUp() {
        achievement = Achievement.builder()
                .id(ACHIEVEMENT_ID)
                .build();
        userAchievement = UserAchievement.builder()
                .achievement(achievement)
                .userId(USER_ID)
                .build();
        achievementProgress = AchievementProgress.builder()
                .id(1L)
                .achievement(achievement)
                .userId(USER_ID)
                .build();
        achievementEvent = AchievementEvent.builder()
                .userId(USER_ID)
                .achievementId(ACHIEVEMENT_ID)
                .build();
    }

    @Test
    public void testUserHasAchievement() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID)).thenReturn(true);
        assertTrue(achievementService.hasAchievement(USER_ID, ACHIEVEMENT_ID));
        verify(userAchievementRepository).existsByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    public void testUserHasNoAchievement() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID)).thenReturn(false);
        assertFalse(achievementService.hasAchievement(USER_ID, ACHIEVEMENT_ID));
        verify(userAchievementRepository).existsByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    public void testCreateProgressIfNecessary() {
        achievementService.createProgressIfNecessary(USER_ID, ACHIEVEMENT_ID);
        verify(achievementProgressRepository).createProgressIfNecessary(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    public void testGetExistsProgress() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID)).thenReturn(Optional.of(achievementProgress));
        assertEquals(achievementService.getProgress(USER_ID, ACHIEVEMENT_ID).getId(), achievementProgress.getId());
        verify(achievementProgressRepository).findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    public void testGetNonExistsProgress() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> achievementService.getProgress(USER_ID, ACHIEVEMENT_ID));
        verify(achievementProgressRepository).findByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    public void testUpdateProgress() {
        achievementService.updateProgress(achievementProgress);
        verify(achievementProgressRepository).save(achievementProgress);
    }

    @Test
    public void testGiveAchievement() {
        when(achievementRepository.findById(ACHIEVEMENT_ID)).thenReturn(Optional.of(achievement));
        achievementService.giveAchievement(USER_ID, ACHIEVEMENT_ID);
        verify(achievementRepository).findById(ACHIEVEMENT_ID);
        verify(userAchievementRepository).save(userAchievement);
        verify(achievementPublisher).publish(achievementEvent);
    }

    @Test
    public void testGiveNonExistsAchievement() {
        when(achievementRepository.findById(ACHIEVEMENT_ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> achievementService.giveAchievement(USER_ID, ACHIEVEMENT_ID));
        verify(achievementRepository).findById(ACHIEVEMENT_ID);
    }
}

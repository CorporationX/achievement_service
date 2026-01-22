package faang.school.achievement.service;

import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceTest {

    @InjectMocks
    AchievementService achievementService;

    @Mock
    AchievementProgressRepository achievementProgressRepository;

    @Mock
    UserAchievementRepository userAchievementRepository;

    final long MENTOR_ID = 10L;
    final long ACHIEVEMENT_ID = 4L;

    @Test
    void returnsFalse_hasAchievement() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(MENTOR_ID, ACHIEVEMENT_ID))
                .thenReturn(false);

        Assertions.assertFalse(achievementService.hasAchievement(MENTOR_ID, ACHIEVEMENT_ID));

        verify(userAchievementRepository, times(1))
                .existsByUserIdAndAchievementId(MENTOR_ID, ACHIEVEMENT_ID);
    }

    @Test
    void returnsTrue_hasAchievement() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(MENTOR_ID, ACHIEVEMENT_ID))
                .thenReturn(true);

        Assertions.assertTrue(achievementService.hasAchievement(MENTOR_ID, ACHIEVEMENT_ID));

        verify(userAchievementRepository, times(1))
                .existsByUserIdAndAchievementId(MENTOR_ID, ACHIEVEMENT_ID);
    }

    @Test
    void returnsProgress() {
        AchievementProgress progress = new AchievementProgress();

        when(achievementProgressRepository.findByUserIdAndAchievementId(MENTOR_ID, ACHIEVEMENT_ID))
                .thenReturn(Optional.of(progress));

        Assertions.assertEquals(achievementService.getProgress(MENTOR_ID, ACHIEVEMENT_ID), progress);
    }

    @Test
    void throwsWhenThereIsNoProgress() {
        Assertions.assertThrows(IllegalStateException.class, () -> achievementService.getProgress(MENTOR_ID, ACHIEVEMENT_ID));
    }

    @Test
    void createsProgressIfNecessary() {
        achievementService.createProgressIfNecessary(MENTOR_ID, ACHIEVEMENT_ID);

        verify(achievementProgressRepository,
                times(1)
        ).createProgressIfNecessary(MENTOR_ID, ACHIEVEMENT_ID);

        verifyNoMoreInteractions(achievementProgressRepository);
    }

    @Test
    void givesAchievement() {
        achievementService.assignAchievementToUser(MENTOR_ID, ACHIEVEMENT_ID);

        verify(userAchievementRepository, times(1)).giveAchievement(MENTOR_ID, ACHIEVEMENT_ID);
        verifyNoMoreInteractions(userAchievementRepository);
    }
}
package faang.school.achievement.service;

import faang.school.achievement.exeption.AchievementNotFoundException;
import faang.school.achievement.exeption.ProgressNotFound;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    AchievementService achievementService;

    private Achievement achievement = new Achievement();
    private AchievementProgress achievementProgress = new AchievementProgress();

    private Long userId = 1L;
    private Long achievementId = 1L;

    @Nested
    class GetAchievementByName {
        @Test
        void success() {
            when(achievementRepository.findByTitle(anyString())).thenReturn(Optional.of(achievement));

            Achievement result = achievementService.getAchievementByName("EXPERT");

            assertNotNull(result);
            assertEquals(achievement, result);
            verify(achievementRepository, times(1)).findByTitle(anyString());
        }

        @Test
        void notFound() {
            when(achievementRepository.findByTitle(anyString())).thenReturn(Optional.empty());

            assertThrows(AchievementNotFoundException.class, () ->
                    achievementService.getAchievementByName(anyString()));
            verify(achievementRepository, times(1)).findByTitle(anyString());
        }
    }

    @Nested
    class HasAchievement {
        @Test
        void successTrue() {
            when(userAchievementRepository.existsByUserIdAndAchievementId(anyLong(), anyLong())).thenReturn(true);

            boolean result = achievementService.hasAchievement(anyLong(), anyLong());
            assertTrue(result);
            verify(userAchievementRepository, times(1)).existsByUserIdAndAchievementId(anyLong(), anyLong());
        }

        @Test
        void successFalse() {
            when(userAchievementRepository.existsByUserIdAndAchievementId(anyLong(), anyLong())).thenReturn(false);

            boolean result = achievementService.hasAchievement(anyLong(), anyLong());
            assertFalse(result);
            verify(userAchievementRepository, times(1)).existsByUserIdAndAchievementId(anyLong(), anyLong());
        }
    }

    @Nested
    class CreateProgressIfNecessary {
        @Test
        void necessaryTrue() {
            when(achievementProgressRepository.existsByUserIdAndAchievementId(anyLong(), anyLong())).thenReturn(true);

            achievementService.createProgressIfNecessary(anyLong(), anyLong());

            verify(achievementProgressRepository, never()).save(any(AchievementProgress.class));
        }

        @Test
        void necessaryFalse() {
            when(achievementProgressRepository.existsByUserIdAndAchievementId(anyLong(), anyLong())).thenReturn(false);
            when(achievementRepository.findById(anyLong())).thenReturn(Optional.of(achievement));

            achievementService.createProgressIfNecessary(anyLong(), anyLong());

            verify(achievementProgressRepository, times(1)).save(any(AchievementProgress.class));
        }

        @Test
        void achievementNotFound() {
            when(achievementProgressRepository.existsByUserIdAndAchievementId(anyLong(), anyLong())).thenReturn(false);
            when(achievementRepository.findById(anyLong())).thenReturn(Optional.empty());


            assertThrows(AchievementNotFoundException.class,
                    () -> achievementService.createProgressIfNecessary(anyLong(), anyLong())
            );

            verify(achievementProgressRepository, never()).save(any(AchievementProgress.class));
        }
    }

    @Nested
    class GetProgress {
        @Test
        public void success() {
            when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)).thenReturn(Optional.of(achievementProgress));

            achievementService.getProgress(userId, achievementId);

            verify(achievementProgressRepository, times(1)).findByUserIdAndAchievementId(userId, achievementId);
        }

        @Test
        public void progressNotFound() {
            when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)).thenReturn(Optional.empty());

            assertThrows(ProgressNotFound.class, () -> achievementService.getProgress(userId, achievementId));

            verify(achievementProgressRepository, times(1)).findByUserIdAndAchievementId(userId, achievementId);
        }
    }

    @Nested
    class SaveProgress {
        @Test
        void saveProgress() {
            when(achievementProgressRepository.save(achievementProgress)).thenReturn(achievementProgress);

            achievementService.saveProgress(achievementProgress);

            verify(achievementProgressRepository, times(1)).save(achievementProgress);
        }
    }

    @Nested
    class GiveAchievement {
        @Test
        void success() {
            when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(achievement));

            achievementService.giveAchievement(userId, achievementId);

            verify(userAchievementRepository, times(1)).save(any(UserAchievement.class));
        }

        @Test
        void achievementNotFound() {
            when(achievementRepository.findById(achievementId)).thenReturn(Optional.empty());

            assertThrows(AchievementNotFoundException.class, () -> achievementService.giveAchievement(userId, achievementId));

            verify(userAchievementRepository, never()).save(any(UserAchievement.class));
        }
    }

}
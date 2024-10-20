package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.AchievementTitle;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

    private Long userId;
    private AchievementTitle title;
    private Long requiredPoints;
    private Achievement achievement;
    private AchievementProgress progress;

    @BeforeEach
    void setUp() {
        userId = 1L;
        title = AchievementTitle.WRITER;
        requiredPoints = 10L;
        achievement = mock(Achievement.class);
        progress = AchievementProgress.builder()
                .achievement(achievement)
                .userId(userId)
                .currentPoints(9)
                .build();
    }

    @Test
    void testHasAchievement_True() {
        when(userAchievementRepository.hasAchievement(userId, title)).thenReturn(true);

        boolean result = achievementService.hasAchievement(userId, title);

        assertTrue(result);
        verify(userAchievementRepository, times(1)).hasAchievement(userId, title);
    }

    @Test
    void testHasAchievement_False() {
        achievementService.hasAchievement(userId, title);

        verify(userAchievementRepository, times(1)).hasAchievement(userId, title);
    }

    @Test
    void testUpdateProgress_NotAchieved() {
        progress.setCurrentPoints(8);

        when(achievementProgressRepository.getProgress(userId, title)).thenReturn(Optional.of(progress));
        when(achievementRepository.findByTitle(title)).thenReturn(Optional.of(achievement));

        achievementService.updateProgress(userId, title, requiredPoints);

        assertEquals(9, progress.getCurrentPoints());
        verify(achievementProgressRepository, times(1)).save(progress);
    }

    @Test
    void testUpdateProgress_Achieved() {
        when(achievementProgressRepository.getProgress(userId, title)).thenReturn(Optional.of(progress));
        when(achievementRepository.findByTitle(title)).thenReturn(Optional.of(achievement));

        achievementService.updateProgress(userId, title, requiredPoints);

        assertEquals(10, progress.getCurrentPoints());
        verify(userAchievementRepository, times(1)).save(any(UserAchievement.class));
    }

    @Test
    void testUpdateProgress_CreateNewProgress() {
        when(achievementProgressRepository.getProgress(userId, title)).thenReturn(Optional.empty());
        when(achievementRepository.findByTitle(title)).thenReturn(Optional.of(achievement));

        achievementService.updateProgress(userId, title, requiredPoints);

        verify(achievementProgressRepository, times(1)).save(any(AchievementProgress.class));
    }
}

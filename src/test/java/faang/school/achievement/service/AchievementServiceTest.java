package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementProgressRecord;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementCode;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.redis.AchievementCache;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {
    @InjectMocks
    private AchievementService achievementService;

    @Mock
    private AchievementRepository achievementRepository;
    @Mock
    private AchievementProgressRepository achievementProgressRepository;
    @Mock
    private UserAchievementRepository userAchievementRepository;
    @Mock
    private AchievementMapper achievementMapper;
    @Mock
    private AchievementCache achievementCache;

    @Test
    void saveAchievementProgress_shouldUpdateExistingProgress() {
        Achievement achievement = new Achievement();
        achievement.setId(1L);
        achievement.setTitle("Java Master");

        AchievementProgressRecord record = new AchievementProgressRecord(achievement, 1L, 10L);

        AchievementProgress existingProgress = new AchievementProgress();
        existingProgress.setCurrentPoints(5L);

        when(achievementProgressRepository.findByUserIdAndAchievementId(1L, 1L))
                .thenReturn(Optional.of(existingProgress));

        AchievementProgress result = achievementService.saveAchievementProgress(record);

        assertEquals(15L, result.getCurrentPoints());
        verify(achievementProgressRepository, never()).save(any());
    }

    @Test
    void saveAchievementProgress_shouldCreateNewProgress() {
        Achievement achievement = new Achievement();
        achievement.setId(2L);
        achievement.setTitle("Newbie");

        AchievementProgressRecord record = new AchievementProgressRecord(achievement, 2L, 20L);

        when(achievementProgressRepository.findByUserIdAndAchievementId(2L, 2L))
                .thenReturn(Optional.empty());

        AchievementProgress mappedProgress = new AchievementProgress();
        mappedProgress.setCurrentPoints(20L);
        when(achievementMapper.toAchievement(record)).thenReturn(mappedProgress);
        when(achievementProgressRepository.save(mappedProgress)).thenReturn(mappedProgress);

        AchievementProgress result = achievementService.saveAchievementProgress(record);

        assertEquals(20L, result.getCurrentPoints());
        verify(achievementProgressRepository).save(mappedProgress);
    }

    @Test
    void assignAchievementIfCompleted_shouldAssignIfCompleted() {
        Achievement achievement = new Achievement();
        achievement.setTitle("Code Guru");

        AchievementProgressRecord record = new AchievementProgressRecord(achievement, 3L, 100L);

        when(achievementRepository.findPointsByTitle("Code Guru")).thenReturn(50L);

        when(achievementMapper.toUserAchievement(record)).thenReturn(any());
        boolean result = achievementService.assignAchievementIfCompleted(record);

        assertTrue(result);
        verify(userAchievementRepository).save(any());
    }

    @Test
    void assignAchievementIfCompleted_shouldNotAssignIfNotCompleted() {
        Achievement achievement = new Achievement();
        achievement.setTitle("Code Novice");

        AchievementProgressRecord record = new AchievementProgressRecord(achievement, 4L, 30L);

        when(achievementRepository.findPointsByTitle("Code Novice")).thenReturn(100L);

        boolean result = achievementService.assignAchievementIfCompleted(record);

        Assertions.assertFalse(result);
        verify(userAchievementRepository, never()).save(any());
    }

    @Test
    void getAchievements_shouldReturnAchievementsFromCache() {
        List<Achievement> achievements = List.of(new Achievement(), new Achievement());

        when(achievementCache.getAchievements()).thenReturn(achievements);

        List<Achievement> result = achievementService.getAchievements();

        assertEquals(2, result.size());
    }

    @Test
    void getAchievementByCode_shouldReturnFromCache() {
        AchievementCode code = AchievementCode.EXPERT;
        Achievement achievement = new Achievement();

        when(achievementCache.getAchievementByCode(code)).thenReturn(achievement);

        Achievement result = achievementService.getAchievementByCode(code);

        assertEquals(achievement, result);
    }
}
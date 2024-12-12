package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.event.achievement.AchievementEvent;
import faang.school.achievement.mapper.achievement.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.AchievementPublisher;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {

    @Mock
    private AchievementCache achievementCache;

    @Spy
    private AchievementMapper achievementMapper = Mappers.getMapper(AchievementMapper.class);

    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private AchievementPublisher achievementPublisher;

    @InjectMocks
    private AchievementService achievementService;

    @Test
    void getTest() {
        String title = "Achievement1";
        Achievement achievementFirst = new Achievement();
        achievementFirst.setTitle("Achievement1");
        when(achievementCache.get(title)).thenReturn(achievementFirst);

        AchievementDto achievementDto = achievementService.get(title);

        assertEquals(achievementFirst.getTitle(), achievementDto.getTitle());
    }

    @Test
    void getAllTest() {
        Achievement achievementFirst = new Achievement();
        achievementFirst.setTitle("Achievement1");
        Achievement achievementSecond = new Achievement();
        achievementSecond.setTitle("Achievement2");
        List<Achievement> achievements = List.of(achievementFirst, achievementSecond);
        when(achievementCache.getAll()).thenReturn(achievements);

        List<AchievementDto> achievementDtos = achievementService.getAll();

        assertEquals(achievementFirst.getTitle(), achievementDtos.get(0).getTitle());
        assertEquals(achievementSecond.getTitle(), achievementDtos.get(1).getTitle());
    }

    @Test
    void hasAchievementTest() {
        long userId = 1L;
        long achievementId = 100L;
        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(true);

        boolean result = achievementService.hasAchievement(userId, achievementId);

        assertTrue(result);
        verify(userAchievementRepository, times(1)).existsByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    void createProgressIfNecessaryTest() {
        long userId = 1L;
        long achievementId = 100L;

        achievementService.createProgressIfNecessary(userId, achievementId);

        verify(achievementProgressRepository, times(1)).createProgressIfNecessary(userId, achievementId);
    }

    @Test
    void getProgressTest() {
        long userId = 1L;
        long achievementId = 100L;
        AchievementProgress progress = new AchievementProgress();
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)).thenReturn(Optional.of(progress));

        AchievementProgress result = achievementService.getProgress(userId, achievementId);

        assertEquals(progress, result);
        verify(achievementProgressRepository, times(1)).findByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    void getProgressNotFoundTest() {
        long userId = 1L;
        long achievementId = 100L;
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> achievementService.getProgress(userId, achievementId));

        verify(achievementProgressRepository).findByUserIdAndAchievementId(userId, achievementId);
    }

    @Test
    void giveAchievementTest() {
        long userId = 1L;
        Achievement achievement = new Achievement();
        achievement.setTitle("Achievement1");
        achievement.setDescription("Test achievement");

        achievementService.giveAchievement(userId, achievement);

        verify(userAchievementRepository).save(any(UserAchievement.class));
        verify(achievementPublisher).publish(any(AchievementEvent.class));
    }

    @Test
    void updateProgressTest() {
        AchievementProgress progress = new AchievementProgress();

        achievementService.updateProgress(progress);

        verify(achievementProgressRepository).save(progress);
    }
}
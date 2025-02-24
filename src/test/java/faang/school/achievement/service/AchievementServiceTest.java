package faang.school.achievement.service;

import faang.school.achievement.dto.achievement.AchievementFilterDto;
import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.mapper.achievement.AchievementMapper;
import faang.school.achievement.mapper.achievement.AchievementProgressMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceTest {

    @Mock
    private AchievementMapper achievementMapper;
    @Mock
    private AchievementProgressMapper achievementProgressMapper;
    @Mock
    private AchievementRepository achievementRepository;
    @Mock
    private UserAchievementRepository userAchievementRepository;
    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @InjectMocks
    private AchievementService achievementService;

    @Test
    public void testGetAllAchievements() {
        AchievementFilterDto filterDto = new AchievementFilterDto();
        achievementService.getAllAchievements(filterDto);
        Mockito.verify(achievementRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testGetAchievementsByUserId() {
        achievementService.getAchievementsByUserId(1L);
        Mockito.verify(userAchievementRepository, Mockito.times(1)).findByUserId(1L);
    }

    @Test
    public void testGetAchievementByIdSuccessCase() {
        Achievement achievement = new Achievement();
        Mockito.when(achievementRepository.findById(1L)).thenReturn(Optional.of(achievement));
        assertDoesNotThrow(() -> achievementService.getAchievementById(1L));
    }

    @Test
    public void testGetAchievementByIdNotExist() {
        Mockito.when(achievementRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> achievementService.getAchievementById(1L));
    }

    @Test
    public void testGetAchievementProgressByUserId() {
        achievementService.getAchievementProgressByUserId(1L);
        Mockito.verify(achievementProgressRepository, Mockito.times(1)).findByUserId(1L);
    }

}

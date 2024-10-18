package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.exception.AchievementException;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.mapper.AchievementProgressMapper;
import faang.school.achievement.mapper.UserAchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.Rarity;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AchievementServiceImplTest {

    @InjectMocks
    private AchievementServiceImpl achievementService;

    @Mock
    private AchievementRepository achievementRepository;
    @Mock
    private UserAchievementRepository userAchievementRepository;
    @Mock
    private AchievementProgressRepository achievementProgressRepository;
    @Mock
    private AchievementMapper achievementMapper;
    @Mock
    private UserAchievementMapper userAchievementMapper;
    @Mock
    private AchievementProgressMapper achievementProgressMapper;

    private Achievement achievement1;
    private Achievement achievement2;
    private UserAchievement userAchievement;
    private AchievementProgress achievementProgress;

    @BeforeEach
    public void setup() {
        achievement1 = new Achievement();
        achievement1.setId(1L);
        achievement1.setTitle("Achievement 1");
        achievement1.setDescription("Description 1");
        achievement1.setRarity(Rarity.COMMON);
        achievement1.setPoints(100);

        achievement2 = new Achievement();
        achievement2.setId(2L);
        achievement2.setTitle("Achievement 2");
        achievement2.setDescription("Description 2");
        achievement2.setRarity(Rarity.UNCOMMON);
        achievement2.setPoints(200);

        userAchievement = new UserAchievement();
        userAchievement.setId(1L);
        userAchievement.setAchievement(achievement1);
        userAchievement.setUserId(1L);

        achievementProgress = new AchievementProgress();
        achievementProgress.setId(1L);
        achievementProgress.setAchievement(achievement1);
        achievementProgress.setUserId(1L);
        achievementProgress.setCurrentPoints(50);
    }

    @Test
    public void testGetAllAchievements() {
        List<Achievement> achievements = new ArrayList<>();
        achievements.add(achievement1);
        achievements.add(achievement2);

        when(achievementRepository.findAll()).thenReturn(achievements);
        when(achievementMapper.toDto(achievement1)).thenReturn(new AchievementDto(1L, "Achievement 1", "Description 1", Rarity.COMMON, 100));
        when(achievementMapper.toDto(achievement2)).thenReturn(new AchievementDto(2L, "Achievement 2", "Description 2", Rarity.UNCOMMON, 200));

        List<AchievementDto> result = achievementService.getAllAchievements();

        assertEquals(2, result.size());
        assertEquals("Achievement 1", result.get(0).getTitle());
        assertEquals("Achievement 2", result.get(1).getTitle());
        verify(achievementRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllUserAchievements() {
        Long userId = 1L;
        List<UserAchievement> userAchievements = Arrays.asList(userAchievement);

        when(userAchievementRepository.findByUserId(userId)).thenReturn(userAchievements);
        when(userAchievementMapper.toDto(userAchievement)).thenReturn(new UserAchievementDto(1L, userId, new AchievementDto(1L, "Achievement 1", "Description 1", Rarity.COMMON, 100)));

        List<UserAchievementDto> result = achievementService.getAllUserAchievements(userId);

        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getUserId());
        assertEquals("Achievement 1", result.get(0).getAchievement().getTitle());
        verify(userAchievementRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void testGetAchievement() {
        Long achievementId = 1L;

        when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(achievement1));
        when(achievementMapper.toDto(achievement1)).thenReturn(new AchievementDto(achievementId, "Achievement 1", "Description 1", Rarity.COMMON, 100));

        AchievementDto result = achievementService.getAchievement(achievementId);

        assertEquals("Achievement 1", result.getTitle());
        verify(achievementRepository, times(1)).findById(achievementId);
    }

    @Test
    public void testGetAchievementNotFound() {
        Long achievementId = 1L;

        when(achievementRepository.findById(achievementId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(AchievementException.class, () -> {
            achievementService.getAchievement(achievementId);
        });

        assertEquals("no achievement found with id " + achievementId, exception.getMessage());
        verify(achievementRepository, times(1)).findById(achievementId);
    }

    @Test
    public void testGetUserAchievementProgress() {
        Long userId = 1L;
        List<AchievementProgress> achievementProgressList = Arrays.asList(achievementProgress);

        when(achievementProgressRepository.findByUserId(userId)).thenReturn(achievementProgressList);
        when(achievementProgressMapper.toDto(achievementProgress)).thenReturn(new AchievementProgressDto(1L, new AchievementDto(1L, "Achievement 1", "Description 1", Rarity.COMMON, 100), userId, 50));

        List<AchievementProgressDto> result = achievementService.getUserAchievementProgress(userId);

        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getUserId());
        assertEquals("Achievement 1", result.get(0).getAchievement().getTitle());
        assertEquals(50, result.get(0).getCurrentPoints());
        verify(achievementProgressRepository, times(1)).findByUserId(userId);
    }
}

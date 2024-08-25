package faang.school.achievement.service;

import faang.school.achievement.config.context.UserContext;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.filter.achievement.AchievementFilter;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.mapper.AchievementProgressMapper;
import faang.school.achievement.mapper.UserAchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import faang.school.achievement.exception.DataNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {

    @Mock
    AchievementRepository achievementRepository;

    @Mock
    UserAchievementRepository userAchievementRepository;

    @Mock
    AchievementProgressRepository achievementProgressRepository;

    @Mock
    AchievementMapper achievementMapper;

    @Mock
    UserAchievementMapper userAchievementMapper;

    @Mock
    AchievementProgressMapper achievementProgressMapper;

    @Mock
    AchievementFilter achievementFilter;

    @Mock
    UserContext userContext;

    @Mock
    AchievementCache achievementCache;

    @InjectMocks
    AchievementService achievementService;

    long userId;
    long achievementId;
    Achievement achievement;
    AchievementDto achievementDto;
    AchievementFilterDto achievementFilterDto;
    UserAchievement userAchievement;
    UserAchievementDto userAchievementDto;
    AchievementProgress achievementProgress;
    AchievementProgressDto achievementProgressDto;
    List<Achievement> achievements;
    List<AchievementDto> achievementsDto;
    List<UserAchievement> userAchievements;
    List<UserAchievementDto> userAchievementsDto;
    List<AchievementProgress> achievementProgresses;
    List<AchievementProgressDto> achievementProgressesDto;
    String achievementTitle;

    @BeforeEach
    void setUp() {
        achievementService = new AchievementService(
                achievementRepository,
                userAchievementRepository,
                achievementMapper,
                achievementCache,
                achievementProgressRepository,
                userAchievementMapper,
                achievementProgressMapper,
                List.of(achievementFilter),
                userContext
        );

        userId = 1;
        achievementId = 1;
        achievement = new Achievement();
        achievementDto = AchievementDto.builder().build();
        achievementFilterDto = AchievementFilterDto.builder().build();
        userAchievement = new UserAchievement();
        userAchievementDto = UserAchievementDto.builder().build();
        achievementProgress = new AchievementProgress();
        achievementProgressDto = AchievementProgressDto.builder().build();
        achievements = List.of(achievement);
        achievementsDto = List.of(achievementDto);
        userAchievements = List.of(userAchievement);
        userAchievementsDto = List.of(userAchievementDto);
        achievementProgresses = List.of(achievementProgress);
        achievementProgressesDto = List.of(achievementProgressDto);
        achievementTitle = "title";
        achievement = new Achievement();
        achievementDto = AchievementDto.builder().build();
    }

    @Test
    @DisplayName("Should return list of UserAchievementDto when retrieving achievements by user ID")
    void getAchievementsByUserId() {
        when(userContext.getUserId()).thenReturn(userId);
        when(userAchievementRepository.findByUserId(userId)).thenReturn(userAchievements);
        when(userAchievementMapper.toDto(userAchievement)).thenReturn(userAchievementDto);

        List<UserAchievementDto> result = achievementService.getAchievementsByUserId();

        verify(userAchievementRepository).findByUserId(userId);
        verify(userAchievementMapper).toDto(userAchievement);
        assertNotNull(result);
        assertEquals(userAchievementsDto, result);
    }

    @Test
    @DisplayName("Should return AchievementDto when retrieving achievement by ID")
    void getAchievementById() {
        when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(achievement));
        when(achievementMapper.toDto(achievement)).thenReturn(achievementDto);

        AchievementDto result = achievementService.getAchievementById(achievementId);

        verify(achievementRepository).findById(achievementId);
        verify(achievementMapper).toDto(achievement);
        assertNotNull(result);
        assertEquals(achievementDto, result);
    }

    @Test
    @DisplayName("Should return list of AchievementProgressDto when retrieving achievement progress by user ID")
    void getAchievementProgressByUserId() {
        when(userContext.getUserId()).thenReturn(userId);
        when(achievementProgressRepository.findByUserId(userId)).thenReturn(achievementProgresses);
        when(achievementProgressMapper.toDto(achievementProgress)).thenReturn(achievementProgressDto);

        List<AchievementProgressDto> result = achievementService.getAchievementProgressByUserId();

        verify(achievementProgressRepository).findByUserId(userId);
        verify(achievementProgressMapper).toDto(achievementProgress);
        assertNotNull(result);
        assertEquals(achievementProgressesDto, result);
    }

    @Test
    @DisplayName("Should return AchievementDto when achievement is found in cache")
    void getAchievementByTitle_FoundInCache() {
        when(achievementCache.getAchievementByTitle(achievementTitle)).thenReturn(Optional.of(achievement));
        when(achievementMapper.toDto(achievement)).thenReturn(achievementDto);

        AchievementDto result = achievementService.getAchievementByTitle(achievementTitle);

        verify(achievementCache).getAchievementByTitle(achievementTitle);
        verify(achievementMapper).toDto(achievement);
        assertNotNull(result);
        assertEquals(achievementDto, result);
    }

    @Test
    @DisplayName("Should return AchievementDto when achievement is found in repository after cache miss")
    void getAchievementByTitle_NotFoundInCache() {
        when(achievementCache.getAchievementByTitle(achievementTitle)).thenReturn(Optional.empty());
        when(achievementRepository.findByTitle(achievementTitle)).thenReturn(Optional.of(achievement));
        when(achievementMapper.toDto(achievement)).thenReturn(achievementDto);

        AchievementDto result = achievementService.getAchievementByTitle(achievementTitle);

        verify(achievementCache).getAchievementByTitle(achievementTitle);
        verify(achievementRepository).findByTitle(achievementTitle);
    }

      @Test
      @DisplayName("Should throw EntityNotFoundException when achievement is not found in cache or repository")
    void getAchievementByTitle_NotFound() {
        when(achievementCache.getAchievementByTitle(achievementTitle)).thenReturn(Optional.empty());
        when(achievementRepository.findByTitle(achievementTitle)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> achievementService.getAchievementByTitle(achievementTitle));
    }

    @Test
    @DisplayName("Test save achievement to user - achievement found")
    public void testSaveAchievementToUserAchievementFound() {
        long achievementId = 1L;
        long userId = 1L;
        Achievement achievement = new Achievement();
        when(achievementRepository.findById(achievementId)).thenReturn(Optional.of(achievement));
        when(userContext.getUserId()).thenReturn(userId);

        achievementService.saveAchievementToUser(achievementId);

        verify(userAchievementRepository).save(any(UserAchievement.class));
    }

    @Test
    @DisplayName("Test save achievement to user - achievement not found")
    public void testSaveAchievementToUser_AchievementNotFound() {
        long achievementId = 1L;
        when(achievementRepository.findById(achievementId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> achievementService.saveAchievementToUser(achievementId));
    }
}
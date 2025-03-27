package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.mapper.AchievementProgressMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {
    private static final long USER_ID = 1L;
    private static final long ACHIEVEMENT_ID = 1L;
    private static final String ACHIEVEMENT_TITLE = "Test Achievement";

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private AchievementRepository repository;

    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @Mock
    private AchievementMapper mapper;

    @Mock
    private AchievementProgressMapper progressMapper;

    @InjectMocks
    private AchievementService achievementService;

    @Test
    void testHasAchievement_ShouldReturnTrueIfAchievementExists() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID)).thenReturn(true);

        boolean result = achievementService.hasAchievement(USER_ID, ACHIEVEMENT_ID);

        assertTrue(result);
        verify(userAchievementRepository).existsByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    void testHasAchievement_ShouldReturnFalseIfAchievementDoesNotExist() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID)).thenReturn(false);

        boolean result = achievementService.hasAchievement(USER_ID, ACHIEVEMENT_ID);

        assertFalse(result);
        verify(userAchievementRepository).existsByUserIdAndAchievementId(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    void testGetProgress_ShouldReturnProgressIfExists() {
        Achievement achievement = Achievement.builder()
                .id(ACHIEVEMENT_ID)
                .title(ACHIEVEMENT_TITLE)
                .build();
        AchievementProgress progress = AchievementProgress.builder()
                .achievement(achievement)
                .userId(USER_ID)
                .currentPoints(5)
                .build();
        when(achievementProgressRepository.findByUserIdAndAchievementIdWithLock(USER_ID, ACHIEVEMENT_ID))
                .thenReturn(Optional.of(progress));

        AchievementProgress result = achievementService.getProgress(USER_ID, ACHIEVEMENT_ID);

        assertNotNull(result);
        assertEquals(progress, result);
        verify(achievementProgressRepository).findByUserIdAndAchievementIdWithLock(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    void testGetProgress_ShouldThrowEntityNotFoundExceptionIfProgressDoesNotExist() {
        when(achievementProgressRepository.findByUserIdAndAchievementIdWithLock(USER_ID, ACHIEVEMENT_ID))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> achievementService.getProgress(USER_ID, ACHIEVEMENT_ID));
        assertEquals("Прогресс по достижению с id " + USER_ID + " не существует", exception.getMessage());
        verify(achievementProgressRepository).findByUserIdAndAchievementIdWithLock(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    void testGiveAchievement_ShouldSaveUserAchievementIfNecessary() {
        Achievement achievement = Achievement.builder()
                .id(ACHIEVEMENT_ID)
                .title(ACHIEVEMENT_TITLE)
                .build();

        achievementService.giveAchievementIfNecessary(USER_ID, achievement);

        verify(userAchievementRepository).giveAchievementIfNecessary(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    void testCreateProgressIfNecessary_ShouldCallRepositoryMethod() {
        achievementService.createProgressIfNecessary(USER_ID, ACHIEVEMENT_ID);

        verify(achievementProgressRepository).createProgressIfNecessary(USER_ID, ACHIEVEMENT_ID);
    }

    @Test
    void testSaveProgress_ShouldCallRepositoryMethod() {
        Achievement achievement = Achievement.builder()
                .id(ACHIEVEMENT_ID)
                .title(ACHIEVEMENT_TITLE)
                .build();
        AchievementProgress progress = AchievementProgress.builder()
                .achievement(achievement)
                .userId(USER_ID)
                .currentPoints(5)
                .build();

        achievementService.saveProgress(progress);

        verify(achievementProgressRepository).save(progress);
    }

    @Test
    void testGetAchievementsByFilters() {
        String name = "Viktor";
        String description = "It's test description.";
        Rarity rarity = Rarity.EPIC;
        List<Achievement> achievements = List.of(new Achievement());
        List<AchievementDto> achievementsDto = List.of(new AchievementDto());

        when(repository.findByFilters(name, description, rarity)).thenReturn(achievements);
        when(mapper.toDtoList(achievements)).thenReturn(achievementsDto);

        List<AchievementDto> result = achievementService.getAllAchievements(name, description, rarity);

        assertNotNull(result);
        assertEquals(achievementsDto, result);
        verify(repository, times(1)).findByFilters(name, description, rarity);
        verify(mapper, times(1)).toDtoList(achievements);
    }

    @Test
    void testGetUserAchievements() {
        List<UserAchievement> userAchievements = List.of(new UserAchievement());
        List<UserAchievementDto> userAchievementsDto = List.of(new UserAchievementDto());
        when(userAchievementRepository.findByUserId(USER_ID)).thenReturn(userAchievements);
        when(mapper.toUserAchievementDtoList(userAchievements)).thenReturn(userAchievementsDto);

        List<UserAchievementDto> result = achievementService.getUserAchievements(USER_ID);

        assertNotNull(result);
        assertEquals(userAchievementsDto, result);
        verify(userAchievementRepository, times(1)).findByUserId(USER_ID);
        verify(mapper, times(1)).toUserAchievementDtoList(userAchievements);
    }

    @Test
    void testGetExistsAchievementById() {
        Achievement achievement = new Achievement();
        AchievementDto achievementDto = new AchievementDto();

        when(repository.findById(ACHIEVEMENT_ID)).thenReturn(Optional.of(achievement));
        when(mapper.toDto(achievement)).thenReturn(achievementDto);

        AchievementDto result = achievementService.getAchievementById(ACHIEVEMENT_ID);

        assertNotNull(result);
        assertEquals(achievementDto, result);
        verify(repository, times(1)).findById(ACHIEVEMENT_ID);
        verify(mapper, times(1)).toDto(achievement);
    }

    @Test
    void testGetDoesNotExistAchievementById() {
        when(repository.findById(ACHIEVEMENT_ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> achievementService.getAchievementById(ACHIEVEMENT_ID));
    }

    @Test
    void testGetUserPendingAchievements() {
        List<AchievementProgress> achievements = List.of(new AchievementProgress());
        List<AchievementProgressDto> achievementsDto = List.of(new AchievementProgressDto());

        when(achievementProgressRepository.findByUserId(USER_ID)).thenReturn(achievements);
        when(progressMapper.toDtoList(achievements)).thenReturn(achievementsDto);

        List<AchievementProgressDto> result = achievementService.getUserPendingAchievements(USER_ID);

        assertNotNull(result);
        assertEquals(achievementsDto, result);
        verify(achievementProgressRepository, times(1)).findByUserId(USER_ID);
        verify(progressMapper, times(1)).toDtoList(achievements);
    }
}
package faang.school.achievement.service;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.exception.ResourceNotFoundException;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static faang.school.achievement.model.Rarity.RARE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {
    private static final String TITLE = "WRITER";
    private static final String DB_TITLE = "DB WRITER";

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private AchievementCache achievementCache;

    @Mock
    private AchievementMapper achievementMapper;

    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @InjectMocks
    private AchievementService achievementService;

    private AchievementDto achievementDto;

    private Achievement achievement;

    @BeforeEach
    void setUpEach() {
        achievementDto = AchievementDto.builder()
                .id(1L)
                .title(TITLE)
                .description("For 100 posts published")
                .rarity(RARE)
                .acceptedUserIds(List.of(2L))
                .points(20L)
                .build();

        achievement = Achievement.builder()
                .id(1L)
                .title(DB_TITLE)
                .description("For 100 posts published")
                .rarity(RARE)
                .userAchievements(List.of(UserAchievement.builder().userId(2L).build()))
                .points(20L)
                .build();
    }

    @Test
    void testGetByTitle_GetFromCache() {
        when(achievementCache.getFromCache(eq(TITLE))).thenReturn(achievementDto);

        AchievementDto result = achievementService.getByTitle(TITLE);

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(achievementDto);

        verify(achievementRepository, never()).findByTitle(TITLE);
    }

    @Test
    void testGetByTitle_GetFromDB() {
        when(achievementCache.getFromCache(eq(TITLE))).thenReturn(null);
        when(achievementRepository.findByTitle(eq(TITLE))).thenReturn(Optional.of(achievement));
        when(achievementMapper.toDto(any(Achievement.class))).thenReturn(achievementDto);
        doNothing().when(achievementCache).addToCache(eq(achievement));

        AchievementDto result = achievementService.getByTitle(TITLE);

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(achievementDto);

        verify(achievementRepository).findByTitle(TITLE);
    }

    @Test
    void testGetByTitle_Exception_TitleNotFound() {
        when(achievementCache.getFromCache(eq(TITLE))).thenReturn(null);
        when(achievementRepository.findByTitle(eq(TITLE))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                achievementService.getByTitle(TITLE)
        );
    }

    @Test
    void testFindByTitle_Success() {
        when(achievementRepository.findByTitle(TITLE)).thenReturn(Optional.of(achievement));

        Achievement result = achievementService.findByTitle(TITLE);

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(achievement);
    }

    @Test
    void testFindByTitle_Exception_AchievementNotFound() {
        when(achievementRepository.findByTitle(TITLE)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                achievementService.findByTitle(TITLE)
        );
    }

    @Test
    void testIsUserHasAchievement_ReturnsTrue() {
        long userId = 1L;
        long achievementId = 2L;

        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(true);

        boolean result = achievementService.isUserHasAchievement(userId, achievementId);

        assertTrue(result);
    }

    @Test
    void testIsUserHasAchievement_ReturnsFalse() {
        long userId = 1L;
        long achievementId = 2L;

        when(userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(false);

        boolean result = achievementService.isUserHasAchievement(userId, achievementId);

        assertFalse(result);
    }

    @Test
    void testCreateProgressIfNecessaryAndReturn_Success() {
        long userId = 1L;
        long achievementId = 2L;
        AchievementProgress achievementProgress = AchievementProgress.builder().build();

        doNothing().when(achievementProgressRepository).createProgressIfNecessary(userId, achievementId);
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)).thenReturn(Optional.of(achievementProgress));

        AchievementProgress result = achievementService.createProgressIfNecessaryAndReturn(userId, achievementId);

        assertThat(result).isEqualTo(achievementProgress);
    }

    @Test
    void testCreateProgressIfNecessaryAndReturn_Exception_AchievementProgressNotFound() {
        long userId = 1L;
        long achievementId = 2L;

        doNothing().when(achievementProgressRepository).createProgressIfNecessary(userId, achievementId);
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                achievementService.createProgressIfNecessaryAndReturn(userId, achievementId)
        );
    }

    @Test
    void testIncrementProgress_Success() {
        AchievementProgress achievementProgress = new AchievementProgress();
        achievementProgress.setCurrentPoints(0);

        achievementService.incrementProgress(achievementProgress);

        assertEquals(1, achievementProgress.getCurrentPoints());

        verify(achievementProgressRepository).save(achievementProgress);
    }

    @Test
    void testGetAllNotCompletedProgresses_Success() {
        List<AchievementProgress> expectedProgresses = new ArrayList<>();
        expectedProgresses.add(new AchievementProgress());
        when(achievementProgressRepository.findAllByCompleted(false)).thenReturn(expectedProgresses);

        List<AchievementProgress> result = achievementService.getAllNotCompletedProgresses();

        assertThat(result).isEqualTo(expectedProgresses);
    }

    @Test
    void testAssignAchievementToUser_Success() {
        AchievementProgress achievementProgress = new AchievementProgress();
        achievementProgress.setUserId(1L);
        achievementProgress.setAchievement(new Achievement());

        achievementService.assignAchievementToUser(achievementProgress);

        assertTrue(achievementProgress.isCompleted());
        verify(achievementProgressRepository).save(achievementProgress);

        UserAchievement userAchievement = UserAchievement.builder()
                .achievement(achievementProgress.getAchievement())
                .userId(achievementProgress.getUserId())
                .build();

        verify(userAchievementRepository).save(userAchievement);
    }
}
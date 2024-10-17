package faang.school.achievement.service.achievement;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.filter.AchievementFilter;
import faang.school.achievement.mapper.AchievementMapperImpl;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.CacheService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementServiceImplTest {

    private static final String ACHIEVEMENTS_CACHE_NAME = "achievements";

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @Mock
    private UserAchievementRepository achievementUserRepository;

    @Mock
    private CacheService<Achievement> cacheService;

    @Spy
    private AchievementMapperImpl achievementMapper;

    @Mock
    private AchievementFilter filter1;

    @Mock
    private AchievementFilter filter2;

    @Spy
    private List<AchievementFilter> filters;

    @InjectMocks
    private AchievementServiceImpl achievementService;

    @Captor
    private ArgumentCaptor<UserAchievement> userAchievementCaptor;

    private final long userId = 1L;
    private final long achievementId = 2L;
    private final Achievement achievement1 = new Achievement();
    private final Achievement achievement2 = new Achievement();
    private List<Achievement> achievements;
    private AchievementFilterDto filterDto;

    @BeforeEach
    void setUp() {
        achievement1.setTitle("Achievement 1");
        achievement1.setId(1L);
        achievement2.setTitle("Achievement 2");
        achievement2.setId(2L);

        achievements = List.of(achievement1, achievement2);
        filterDto = new AchievementFilterDto();
        ReflectionTestUtils.setField(achievementService, "achievementFilters", List.of(filter1, filter2));
    }


    @Test
    void initAchievements_shouldCacheAllAchievements() {
        Achievement first = Achievement.builder().title("first").build();
        Achievement second = Achievement.builder().title("second").build();
        List<Achievement> achievements = List.of(first, second);
        when(achievementRepository.findAll()).thenReturn(achievements);

        achievementService.initAchievements();

        verify(cacheService).put(ACHIEVEMENTS_CACHE_NAME, first.getTitle(), first);
        verify(cacheService).put(ACHIEVEMENTS_CACHE_NAME, second.getTitle(), second);
    }

    @Test
    void hasAchievement_shouldReturnTrueIfAchievementExists() {
        when(achievementUserRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(true);

        boolean result = achievementService.hasAchievement(userId, achievementId);

        assertTrue(result);
    }

    @Test
    void hasAchievement_shouldReturnFalseIfAchievementDoesNotExist() {
        when(achievementUserRepository.existsByUserIdAndAchievementId(userId, achievementId)).thenReturn(false);

        boolean result = achievementService.hasAchievement(userId, achievementId);

        assertFalse(result);
    }

    @Test
    void getProgress_shouldReturnProgressWhenFound() {
        AchievementProgress expectedProgress = new AchievementProgress();
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.of(expectedProgress));

        AchievementProgress result = achievementService.getProgress(userId, achievementId);

        assertEquals(expectedProgress, result);
    }

    @Test
    void getProgress_shouldThrowExceptionWhenNotFound() {
        String message = "Achievement %d progress not found".formatted(achievementId);
        when(achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> achievementService.getProgress(userId, achievementId));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void createProgressIfNecessary_shouldCallRepositoryMethod() {
        achievementService.createProgressIfNecessary(userId, achievementId);

        verify(achievementProgressRepository).createProgressIfNecessary(userId, achievementId);
    }

    @Test
    void giveAchievement_shouldSaveUserAchievement() {
        Achievement achievement = new Achievement();
        UserAchievement correctUserAchievement = UserAchievement.builder()
                .userId(userId)
                .achievement(achievement)
                .build();

        achievementService.giveAchievement(userId, achievement);

        verify(achievementUserRepository).save(userAchievementCaptor.capture());
        assertEquals(correctUserAchievement, userAchievementCaptor.getValue());
    }

    @Test
    void testGetAchievements_ShouldReturnFilteredAchievements() {
        AchievementDto achievementDto1 = new AchievementDto();
        String title = "Achievement 1";
        achievementDto1.setTitle(title);
        achievementDto1.setId(1L);
        List<AchievementDto> correctResult = List.of(achievementDto1);

        when(filter1.isAccepted(filterDto)).thenReturn(true);
        when(filter2.isAccepted(filterDto)).thenReturn(false);
        when(filter1.apply(any(), eq(filterDto))).thenAnswer(invocation -> {
            Stream<Achievement> stream = invocation.getArgument(0);
            return stream.filter(achievement -> achievement.getTitle().equals(title));
        });
        when(cacheService.getValuesFromMap(ACHIEVEMENTS_CACHE_NAME, Achievement.class)).thenReturn(achievements);

        List<AchievementDto> result = achievementService.getAchievements(filterDto);

        verify(filter1).apply(any(), eq(filterDto));
        verify(filter2, never()).apply(any(), eq(filterDto));
        assertEquals(correctResult, result);
    }

    @Test
    void testGetAchievements_ShouldReturnAllAchievementsWhenNoFiltersAccepted() {
        List<AchievementDto> correctResult = getAchievementDtos();

        when(filter1.isAccepted(filterDto)).thenReturn(false);
        when(filter2.isAccepted(filterDto)).thenReturn(false);
        when(cacheService.getValuesFromMap(ACHIEVEMENTS_CACHE_NAME, Achievement.class)).thenReturn(achievements);

        List<AchievementDto> result = achievementService.getAchievements(filterDto);

        verify(filter1, never()).apply(any(), eq(filterDto));
        verify(filter2, never()).apply(any(), eq(filterDto));
        assertEquals(correctResult, result);
    }

    @Test
    void testGetAchievements_ShouldReturnEmptyListWhenNoAchievements() {
        when(cacheService.getValuesFromMap(ACHIEVEMENTS_CACHE_NAME, Achievement.class)).thenReturn(List.of());

        List<AchievementDto> result = achievementService.getAchievements(filterDto);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAchievementsBy_ShouldReturnMappedAchievements() {
        List<AchievementDto> correctResult = getAchievementDtos();
        when(achievementUserRepository.findByUserIdAchievements(userId)).thenReturn(achievements);

        List<AchievementDto> result = achievementService.getAchievementsBy(userId);

        verify(achievementUserRepository).findByUserIdAchievements(userId);
        verify(achievementMapper).toDto(achievements);
        assertEquals(correctResult, result);
    }

    @Test
    void testGetAchievementsBy_ShouldReturnEmptyListWhenNoAchievementsFound() {
        when(achievementUserRepository.findByUserIdAchievements(userId)).thenReturn(List.of());

        List<AchievementDto> result = achievementService.getAchievementsBy(userId);

        verify(achievementUserRepository).findByUserIdAchievements(userId);
        verify(achievementMapper).toDto(List.of());
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAchievement_ShouldReturnAchievementWhenFoundInCache() {
        when(cacheService.getValuesFromMap(ACHIEVEMENTS_CACHE_NAME, Achievement.class)).thenReturn(achievements);

        AchievementDto result = achievementService.getAchievement(achievementId);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Achievement 2", result.getTitle());

        verify(cacheService).getValuesFromMap(ACHIEVEMENTS_CACHE_NAME, Achievement.class);
        verify(achievementMapper).toDto(achievement2);
    }

    @Test
    void testGetAchievement_ShouldThrowEntityNotFoundExceptionWhenNotFoundInCache() {
        String message = "Achievement 123 not found";
        when(cacheService.getValuesFromMap(ACHIEVEMENTS_CACHE_NAME, Achievement.class)).thenReturn(achievements);

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> achievementService.getAchievement(123L));

        assertEquals(message, exception.getMessage());
        verify(cacheService).getValuesFromMap(ACHIEVEMENTS_CACHE_NAME, Achievement.class);
        verify(achievementMapper, never()).toDto(achievement1);
    }

    @Test
    void testGetNotReceivedAchievements_ShouldReturnNotReceivedAchievements() {
        List<AchievementDto> correctResult = getAchievementDtos();
        when(achievementRepository.findUnobtainedAchievementsWithProgressByUserId(userId)).thenReturn(achievements);

        List<AchievementDto> result = achievementService.getNotReceivedAchievements(userId);

        assertEquals(correctResult, result);
        verify(achievementRepository).findUnobtainedAchievementsWithProgressByUserId(userId);
        verify(achievementMapper).toDto(achievements);
    }

    @Test
    void testGetNotReceivedAchievements_ShouldReturnEmptyListWhenNoAchievements() {
        List<Achievement> notReceivedAchievements = List.of();
        when(achievementRepository.findUnobtainedAchievementsWithProgressByUserId(userId)).thenReturn(notReceivedAchievements);

        List<AchievementDto> result = achievementService.getNotReceivedAchievements(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(achievementRepository).findUnobtainedAchievementsWithProgressByUserId(userId);
        verify(achievementMapper).toDto(notReceivedAchievements);
    }

    private List<AchievementDto> getAchievementDtos() {
        AchievementDto achievementDto1 = new AchievementDto(), achievementDto2 = new AchievementDto();
        achievementDto1.setTitle("Achievement 1");
        achievementDto2.setTitle("Achievement 2");
        achievementDto1.setId(1L);
        achievementDto2.setId(2L);
        return List.of(achievementDto1, achievementDto2);
    }
}

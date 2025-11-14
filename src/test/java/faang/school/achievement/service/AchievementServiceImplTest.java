package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.filter.AchievementDescriptionFilter;
import faang.school.achievement.filter.AchievementFilter;
import faang.school.achievement.filter.AchievementRarityFilter;
import faang.school.achievement.filter.AchievementTitleFilter;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceImplTest {
    //    @InjectMocks
    private AchievementServiceImpl achievementService;
    @Mock
    private AchievementRepository achievementRepository;
    @Spy
    private AchievementMapper achievementMapper = Mappers.getMapper(AchievementMapper.class);
    @Mock
    private AchievementProgressRepository achievementProgressRepository;
    @Mock
    private UserAchievementRepository userAchievementRepository;

    private final List<AchievementFilter> achievementFilters = /*new ArrayList<>(*/List.of(
            new AchievementTitleFilter(),
            new AchievementDescriptionFilter(),
            new AchievementRarityFilter()
    );

    private Achievement achievement;
    private UserAchievement userAchievement;
    private AchievementProgress achievementProgress;

    @BeforeEach
    void setUp() {
        this.achievementService = new AchievementServiceImpl(
                achievementRepository,
                achievementMapper,
                achievementProgressRepository,
                userAchievementRepository,
                achievementFilters
        );

        achievement = Achievement.builder()
                .id(1L)
                .title("COLLECTOR")
                .description("For 100 goals")
                .rarity(Rarity.EPIC)
                .points(15L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userAchievement = UserAchievement.builder()
                .id(1L)
                .achievement(achievement)
                .userId(2L)
                .createdAt(LocalDateTime.now())
                .build();

        achievementProgress = AchievementProgress.builder()
                .id(1L)
                .achievement(achievement)
                .userId(3L)
                .currentPoints(42L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(0)
                .build();
    }

    @Test
    @DisplayName("Должен вернуть отфильтрованные достижения")
    void testGetFilteredAchievements_ByTitle() {
        when(achievementRepository.findAll()).thenReturn(List.of(achievement));
        AchievementFilterDto filterDto = new AchievementFilterDto("COLLECTOR", null, null);

        List<AchievementDto> result = achievementService.getFilteredAchievements(filterDto);

        assertEquals(1, result.size());
        assertEquals("COLLECTOR", result.get(0).title());
        verify(achievementRepository).findAll();
    }

    @Test
    @DisplayName("Должен вернуть все достижения при отсутствии фильтров")
    void testGetFilteredAchievements_NoFilters() {
        when(achievementRepository.findAll()).thenReturn(List.of(achievement));
        AchievementFilterDto filterDto = new AchievementFilterDto(null, null, null);

        List<AchievementDto> result = achievementService.getFilteredAchievements(filterDto);

        assertEquals(1, result.size());
        assertNotNull(result.get(0).title());
        assertEquals("COLLECTOR", result.get(0).title());
        verify(achievementRepository).findAll();
    }

    @Test
    @DisplayName("Должен вернуть пустой список, если фильтры не совпадают")
    void testGetFilteredAchievements_NoMatch() {
        when(achievementRepository.findAll()).thenReturn(List.of(achievement));
        AchievementFilterDto filterDto = new AchievementFilterDto("unknown", null, null);

        List<AchievementDto> result = achievementService.getFilteredAchievements(filterDto);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Должен вернуть достижение по ID")
    void testGetAchievementById_Success() {
        when(achievementRepository.findById(1L)).thenReturn(Optional.of(achievement));

        AchievementDto result = achievementService.getAchievementById(1L);

        assertNotNull(result);
        assertEquals("COLLECTOR", result.title());
        assertEquals(1L, result.id());
        verify(achievementRepository).findById(1L);
    }

    @Test
    @DisplayName("Должен выбросить исключение, если достижение не найдено")
    void testGetAchievementById_NotFound() {
        when(achievementRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> achievementService.getAchievementById(999L));
        verify(achievementRepository).findById(999L);
    }

    @Test
    @DisplayName("Должен вернуть список полученных достижений пользователя")
    void testGetUserAchievements() {
        when(userAchievementRepository.findByUserId(2L)).thenReturn(List.of(userAchievement));

        List<AchievementDto> result = achievementService.getUserAchievements(2L);

        assertEquals(1, result.size());
        assertEquals("COLLECTOR", result.get(0).title());
        verify(userAchievementRepository).findByUserId(2L);
    }

    @Test
    @DisplayName("Должен вернуть пустой список, если пользователь не имеет достижений")
    void testGetUserAchievements_Empty() {
        when(userAchievementRepository.findByUserId(2L)).thenReturn(List.of());

        List<AchievementDto> result = achievementService.getUserAchievements(2L);

        assertTrue(result.isEmpty());
        verify(userAchievementRepository).findByUserId(2L);
    }

    @Test
    @DisplayName("Должен вернуть прогресс по неполученным достижениям")
    void testGetUnearnedAchievementsWithProgress() {
        when(userAchievementRepository.findByUserId(3L)).thenReturn(List.of()); // нет полученных
        when(achievementProgressRepository.findByUserId(3L)).thenReturn(List.of(achievementProgress));

        List<AchievementProgressDto> progress = achievementService.getUnearnedAchievementsWithProgress(3L);

        assertEquals(1, progress.size());
        AchievementProgressDto dto = progress.get(0);
        assertEquals(1L, dto.achievementId());
        assertEquals(3L, dto.userId());
        assertEquals(42L, dto.currentProgress());
        assertEquals(100L, dto.targetProgress()); // извлечено из "For 100 goals"
        verify(userAchievementRepository).findByUserId(3L);
        verify(achievementProgressRepository).findByUserId(3L);
    }

    @Test
    @DisplayName("Должен исключить уже полученные достижения из прогресса")
    void testGetUnearnedAchievementsWithProgress_OnlyUnearned() {
        Achievement earnedAchievement = achievement;
        UserAchievement earned = UserAchievement.builder()
                .achievement(earnedAchievement)
                .userId(12L)
                .build();

        AchievementProgress progressForEarned = AchievementProgress.builder()
                .achievement(earnedAchievement)
                .userId(12L)
                .currentPoints(42L)
                .build();

        when(userAchievementRepository.findByUserId(12L)).thenReturn(List.of(earned));
        when(achievementProgressRepository.findByUserId(12L)).thenReturn(List.of(progressForEarned));

        List<AchievementProgressDto> result = achievementService.getUnearnedAchievementsWithProgress(12L);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Должен корректно извлекать число из описания с пробелами")
    void testExtractTargetFromDescription_WithSpaces() {
        Achievement achievement = Achievement.builder()
                .id(6L)
                .title("CELEBRITY")
                .description("For 1 000 000 subscribers") // → 1000000
                .build();

        AchievementProgress progress = AchievementProgress.builder()
                .achievement(achievement)
                .userId(11L)
                .currentPoints(500)
                .build();

        when(userAchievementRepository.findByUserId(11L)).thenReturn(List.of());
        when(achievementProgressRepository.findByUserId(11L)).thenReturn(List.of(progress));

        List<AchievementProgressDto> result = achievementService.getUnearnedAchievementsWithProgress(11L);

        assertEquals(1, result.size());
        assertEquals(1000000L, result.get(0).targetProgress());
    }

    @Test
    @DisplayName("Должен фильтровать по всем полям одновременно")
    void testGetFilteredAchievements_AllFilters() {
        when(achievementRepository.findAll()).thenReturn(List.of(achievement));
        AchievementFilterDto filterDto = new AchievementFilterDto("COLLECTOR", "goals", Rarity.EPIC);

        List<AchievementDto> result = achievementService.getFilteredAchievements(filterDto);

        assertEquals(1, result.size());
        assertEquals("COLLECTOR", result.get(0).title());
    }
}

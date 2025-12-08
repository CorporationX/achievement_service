package faang.school.achievement.service;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressResponseDto;
import faang.school.achievement.dto.AchievementResponseDto;
import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.filters.AchievementFilter;
import faang.school.achievement.filters.DescriptionFilter;
import faang.school.achievement.filters.RarityFilter;
import faang.school.achievement.filters.TitleFilter;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.mapper.AchievementProgressMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.model.UserAchievementStatus;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class AchievementServiceImplTest extends DataForTests {

    @Spy
    private AchievementProgressMapper achievementProgressMapper = Mappers.getMapper(AchievementProgressMapper.class);
    @Spy
    private AchievementMapper achievementMapper = Mappers.getMapper(AchievementMapper.class);
    @Mock
    private AchievementCache achievementCache;
    @Mock
    private AchievementProgressRepository achievementProgressRepository;
    @Mock
    private UserAchievementRepository userAchievementRepository;
    @Spy
    private AchievementFilter descriptionAchievementFilter = new DescriptionFilter();
    @Spy
    private AchievementFilter rarityAchievementFilter = new RarityFilter();
    @Spy
    private AchievementFilter titleAchievementFilter = new TitleFilter();
    private AchievementService achievementService;

    @BeforeEach
    void configureTest() {
        List<AchievementFilter> achievementFilters = List.of(descriptionAchievementFilter, rarityAchievementFilter,
                                                             titleAchievementFilter);

        achievementService = Mockito.spy(
            new AchievementServiceImpl(achievementCache, achievementProgressRepository, userAchievementRepository,
                                       achievementMapper, achievementProgressMapper, achievementFilters));
    }

    @Test
    void getAllAchievementsUser_returnsDtosWhenUserHasAchievements() {
        UserAchievement userAchievement1 = UserAchievement.builder().userId(USER_ID_1).achievement(achievementExpert)
            .build();

        UserAchievement userAchievement2 = UserAchievement.builder().userId(USER_ID_1)
            .achievement(achievementMrProductivity).build();

        List<AchievementResponseDto> allAchievementsUser = Stream.of(achievementExpert, achievementMrProductivity)
            .map(achievementMapper::toAchievementResponseDto).toList();

        when(userAchievementRepository.findByUserId(USER_ID_1)).thenReturn(List.of(userAchievement1, userAchievement2));

        List<AchievementResponseDto> resultAllAchievementsUser = achievementService.getAllAchievementsUser(USER_ID_1);

        assertEquals(new HashSet<>(allAchievementsUser), new HashSet<>(resultAllAchievementsUser));
        verify(userAchievementRepository).findByUserId(USER_ID_1);
    }

    @Test
    void getAllAchievementsUser_returnsEmptyListWhenUserHasNoAchievements() {
        when(userAchievementRepository.findByUserId(USER_ID_1)).thenReturn(List.of());

        List<AchievementResponseDto> result = achievementService.getAllAchievementsUser(USER_ID_1);

        assertTrue(result.isEmpty());
        verify(userAchievementRepository).findByUserId(USER_ID_1);
    }

    Stream<Arguments> validArgsGetByFilters() {
        return Stream.of(Arguments.of(new AchievementFilterDto(null, null, null),
                                      allAchievements, "All achievement"),
                         Arguments.of(new AchievementFilterDto("collector", null, null),
                                      List.of(achievementCollector),
                                      "Filter by title"),
                         Arguments.of(new AchievementFilterDto(null, "For 1000 comments",
                                                               null),
                                      List.of(achievementExpert), "Filter by description"),
                         Arguments.of(new AchievementFilterDto(null, "10", null),
                                      List.of(achievementCollector, achievementMrProductivity, achievementExpert,
                                              achievementManager, achievementWriter),
                                      "Filter by title for all those containing 10"),
                         Arguments.of(new AchievementFilterDto(null, null, Rarity.RARE),
                                      List.of(achievementManager, achievementWriter), "Filter by rarity"),
                         Arguments.of(new AchievementFilterDto(null, "100", Rarity.RARE),
                                      List.of(achievementWriter),
                                      "Filter by rarity and description"),
                         Arguments.of(new AchievementFilterDto("MANAGER", "10", Rarity.RARE),
                                      List.of(achievementManager), "Filter by rarity and description and title"));
    }

    @ParameterizedTest(name = "{index} ➜ {2}")
    @MethodSource("validArgsGetByFilters")
    void getAllAchievements_filters(AchievementFilterDto achievementFilterDto, List<Achievement> achievements,
                                    String testDescription) {
        when(achievementCache.getAll()).thenReturn(allAchievements);

        List<AchievementResponseDto> achievementsResponseDto = achievements.stream()
            .map(achievementMapper::toAchievementResponseDto).toList();

        List<AchievementResponseDto> resultFiltredAllAchievements = achievementService.getAllAchievements(
            achievementFilterDto);
        assertEquals(new HashSet<>(resultFiltredAllAchievements), new HashSet<>(achievementsResponseDto));
        verify(achievementCache, times(1)).getAll();
    }

    @Test
    void getAllAchievementsUser_userHasTwoAchievements() {
        List<AchievementResponseDto> achievementsResponseDto = Stream.of(achievementMrProductivity, achievementHandsome)
            .map(achievementMapper::toAchievementResponseDto).toList();

        when(userAchievementRepository.findByUserId(USER_ID_2)).thenReturn(
            List.of(userAchievementId2, userAchievementId3));

        List<AchievementResponseDto> resultFiltredAllAchievements = achievementService.getAllAchievementsUser(
            USER_ID_2);
        assertEquals(new HashSet<>(resultFiltredAllAchievements), new HashSet<>(achievementsResponseDto));
    }

    @Test
    void getAllAchievementsUser_userIsAbsent() {
        when(userAchievementRepository.findByUserId(UNKNOWN_ID)).thenReturn(List.of());
        List<AchievementResponseDto> resultFiltredAllAchievements = achievementService.getAllAchievementsUser(
            UNKNOWN_ID);
        assertEquals(resultFiltredAllAchievements, List.of());
    }

    @Test
    void getAchievementsById_receivingAnAchievementByItsId() {
        AchievementResponseDto achievementResponseDto = achievementMapper.toAchievementResponseDto(
            achievementCollector);

        when(achievementCache.getById(ACHIEVEMENT_ID_1)).thenReturn(Optional.of(achievementCollector));
        AchievementResponseDto resultFilteredAllAchievements = achievementService.getAchievementsById(ACHIEVEMENT_ID_1);

        assertEquals(resultFilteredAllAchievements, achievementResponseDto);
    }

    @Test
    void getAchievementsById_achievementMissing() {
        when(achievementCache.getById(UNKNOWN_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> achievementService.getAchievementsById(UNKNOWN_ID));

        verifyNoInteractions(achievementMapper);
    }

    Stream<Arguments> providePendingAchievementsTestData() {
        return Stream.of(Arguments.of(
                             List.of(achievementProgressId3CurrentPoints4, achievementProgressId7CurrentPoints8,
                                     achievementProgressId2CurrentPoints15, achievementProgressId1CurrentPoints15),
                             Stream.of(achievementProgressId2CurrentPoints15, achievementProgressId7CurrentPoints8,
                                       achievementProgressId3CurrentPoints4, achievementProgressId4CurrentPoints0,
                                       achievementProgressId5CurrentPoints0, achievementProgressId6CurrentPoints0,
                                       achievementProgressId8CurrentPoints0), USER_ID_1, UserAchievementStatus.PENDING,
                             Stream.of(achievementCollector), "user with partial progress"),

                         Arguments.of(List.of(), allAchievementProgressCurrentPoints0.stream(), UNKNOWN_ID,
                                      UserAchievementStatus.PENDING, Stream.of(), "user with no achievements")

                        );
    }

    @ParameterizedTest(name = "[{index}] -> {5}")
    @MethodSource("providePendingAchievementsTestData")
    void getUserPendingAchievementsWithProgress_allUnearnedUserAchievementsWithProgress(
        List<AchievementProgress> achievementsProgress, Stream<AchievementProgress> resultAchievementsProgress,
        long userId, UserAchievementStatus status, Stream<Achievement> allAchievementUser, String testDescription) {

        List<AchievementResponseDto> achievementsResponseDto = allAchievements.stream()
            .map(achievementMapper::toAchievementResponseDto).toList();

        List<AchievementResponseDto> allAchievementsUser = allAchievementUser.map(
            achievementMapper::toAchievementResponseDto).toList();

        doReturn(achievementsResponseDto).when(achievementService).getAllAchievements(any(AchievementFilterDto.class));
        doReturn(allAchievementsUser).when(achievementService).getAllAchievementsUser(userId);
        when(achievementProgressRepository.findByUserId(userId)).thenReturn(achievementsProgress);

        List<AchievementProgressResponseDto> resultAchievementsProgressResponseDto =
            achievementService.getUserPendingAchievementsWithProgress(
                userId, status);

        List<AchievementProgressResponseDto> resultAchievementsProgressDto = resultAchievementsProgress.map(
            achievementProgressMapper::toAchievementProgressResponseDto).toList();
        assertEquals(new HashSet<>(resultAchievementsProgressDto),
                     new HashSet<>(resultAchievementsProgressResponseDto));
    }

    @Test
    void hasAchievement_returnsTrueWhenUserHasAchievement() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(USER_ID_1, ACHIEVEMENT_ID_1))
            .thenReturn(true);

        boolean result = achievementService.hasAchievement(USER_ID_1, ACHIEVEMENT_ID_1);

        assertTrue(result);
        verify(userAchievementRepository).existsByUserIdAndAchievementId(USER_ID_1, ACHIEVEMENT_ID_1);
    }

    @Test
    void hasAchievement_returnsFalseWhenUserDoesNotHaveAchievement() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(USER_ID_1, ACHIEVEMENT_ID_1))
            .thenReturn(false);

        boolean result = achievementService.hasAchievement(USER_ID_1, ACHIEVEMENT_ID_1);

        assertFalse(result);
        verify(userAchievementRepository).existsByUserIdAndAchievementId(USER_ID_1, ACHIEVEMENT_ID_1);
    }

    @Test
    void createProgressIfNecessary_callsRepository() {
        achievementService.createProgressIfNecessary(USER_ID_1, ACHIEVEMENT_ID_1);
        verify(achievementProgressRepository).createProgressIfNecessary(USER_ID_1, ACHIEVEMENT_ID_1);
    }

    @Test
    void saveProgress_callsRepositorySave() {
        AchievementProgress progress = new AchievementProgress();
        achievementService.saveProgress(progress);
        verify(achievementProgressRepository).save(progress);
    }

    @Test
    void getProgress_returnsOptionalProgressWhenFound() {
        AchievementProgress progress = new AchievementProgress();
        when(achievementProgressRepository.findByUserIdAndAchievementId(USER_ID_1, ACHIEVEMENT_ID_1)).thenReturn(
            Optional.of(progress));

        Optional<AchievementProgress> result = achievementService.getProgress(USER_ID_1, ACHIEVEMENT_ID_1);

        assertTrue(result.isPresent());
        assertEquals(progress, result.get());
        verify(achievementProgressRepository).findByUserIdAndAchievementId(USER_ID_1, ACHIEVEMENT_ID_1);
    }

    @Test
    void getProgress_returnsEmptyOptionalWhenNotFound() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(USER_ID_1, ACHIEVEMENT_ID_1)).thenReturn(
            Optional.empty());

        Optional<AchievementProgress> result = achievementService.getProgress(USER_ID_1, ACHIEVEMENT_ID_1);

        assertTrue(result.isEmpty());
        verify(achievementProgressRepository).findByUserIdAndAchievementId(USER_ID_1, ACHIEVEMENT_ID_1);
    }

    @Test
    void giveAchievement_savesUserAchievementWhenAchievementExists() {
        when(achievementCache.getById(ACHIEVEMENT_ID_1)).thenReturn(Optional.of(achievementCollector));

        achievementService.giveAchievement(USER_ID_1, ACHIEVEMENT_ID_1);

        ArgumentCaptor<UserAchievement> captor = ArgumentCaptor.forClass(UserAchievement.class);
        verify(userAchievementRepository).save(captor.capture());

        UserAchievement savedUserAchievement = captor.getValue();
        assertEquals(USER_ID_1, savedUserAchievement.getUserId());
        assertEquals(achievementCollector, savedUserAchievement.getAchievement());
    }

    @Test
    void giveAchievement_throwsEntityNotFoundExceptionWhenAchievementMissing() {
        when(achievementCache.getById(UNKNOWN_ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> achievementService.giveAchievement(USER_ID_1, UNKNOWN_ID));
        verifyNoInteractions(userAchievementRepository);
    }
}
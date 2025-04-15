package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.exception.AchievementNotFoundException;
import faang.school.achievement.exception.EmptyFilterException;
import faang.school.achievement.exception.ExceptionMessage;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceTest {

    @Mock
    AchievementRepository achievementRepository;

    @Mock
    UserAchievementRepository userAchievementRepository;

    @Mock
    AchievementProgressRepository achievementProgressRepository;

    @Mock
    AchievementMapper achievementMapper;

    @Mock
    AchievementProgressMapper achievementProgressMapper;

    @Mock
    UserAchievementMapper userAchievementMapper;

    @InjectMocks
    AchievementService achievementService;

    private long idForSearch;

    @BeforeEach
    public void setUp() {
        idForSearch = 1L;
    }

    @Test
    void testFindAllSuccess() {
        List<Achievement> achievements = List.of(new Achievement(), new Achievement());

        when(achievementRepository.findAll()).thenReturn(achievements);
        when(achievementMapper.toDto(any(Achievement.class))).thenReturn(createAchievementDto());

        var result = achievementService.findAll();

        assertEquals(achievements.size(), result.size());
    }

    @Test
    @DisplayName("Negative: error when filter is empty")
    void testFindFilteredAchievementsNegative() {
        AchievementFilterDto filter = new AchievementFilterDto(null, null, null);

        assertException(() -> achievementService.findFilteredAchievements(filter), EmptyFilterException.class,
                ExceptionMessage.EMPTY_FILTER.getMessage());
    }

    @Test
    void testFindFilteredAchievementsSuccess() {
        AchievementFilterDto filter = createAchievementFilterDto();
        List<Achievement> achievements = List.of(createAchievement(), createAchievement());

        when(achievementRepository.findAchievementByFilters(filter.title(), filter.description(), filter.rarity())).thenReturn(achievements);
        when(achievementMapper.toDto(any(Achievement.class))).thenReturn(createAchievementDto());

        var result = achievementService.findFilteredAchievements(filter);

        assertEquals(achievements.size(), result.size());
    }

    @Test
    void testFindAchievementsByUserIdSuccess() {
        List<UserAchievement> userAchievements = List.of(new UserAchievement(), new UserAchievement());

        when(userAchievementRepository.findByUserId(idForSearch)).thenReturn(userAchievements);
        when(userAchievementMapper.toDto(any(UserAchievement.class))).thenReturn(createUserAchievementDto());

        var result = achievementService.findAchievementsByUserId(idForSearch);

        assertEquals(userAchievements.size(), result.size());
    }

    @Test
    @DisplayName("Negative: error when achievement not found")
    void testFindAchievementByIdNegative() {
        when(achievementRepository.findById(idForSearch)).thenReturn(Optional.empty());

        assertException(() -> achievementService.findById(idForSearch), AchievementNotFoundException.class,
                ExceptionMessage.ACHIEVEMENT_NOT_FOUND.formatMessage(idForSearch));
    }

    @Test
    void testFindAchievementByIdSuccess() {
        AchievementDto achievementDto = createAchievementDto();
        when(achievementRepository.findById(idForSearch)).thenReturn(Optional.of(new Achievement()));
        when(achievementMapper.toDto(any(Achievement.class))).thenReturn(achievementDto);

        var result = achievementService.findById(idForSearch);

        assertEquals(achievementDto, result);
    }

    @Test
    void testFindProcessingAchievementsByUserId() {
        List<AchievementProgress> achievements = List.of(new AchievementProgress(), new AchievementProgress());

        when(achievementProgressRepository.findByUserId(idForSearch)).thenReturn(achievements);
        when(achievementProgressMapper.toDto(any(AchievementProgress.class))).thenReturn(createAchievementProgressDto());

        var result = achievementService.findProcessingAchievementsByUserId(idForSearch);

        assertEquals(result.size(), achievements.size());
    }

    private void assertException(Executable executable, Class<? extends Exception> expectedException, String expectedMessage) {
        var exception = assertThrows(expectedException, executable);

        assertEquals(exception.getMessage(), expectedMessage);
    }

    private Achievement createAchievement() {
        return new Achievement();
    }

    private AchievementDto createAchievementDto() {
        return AchievementDto.builder()
                .id(1L)
                .title("test")
                .description("test")
                .rarity(Rarity.COMMON)
                .points(10)
                .build();
    }

    private AchievementFilterDto createAchievementFilterDto() {
        return new AchievementFilterDto("test", "test", Rarity.COMMON);
    }

    private UserAchievementDto createUserAchievementDto() {
        return new UserAchievementDto(1L, createAchievementDto());
    }

    private AchievementProgressDto createAchievementProgressDto() {
        return new AchievementProgressDto(1L, 10, createAchievementDto());
    }

}

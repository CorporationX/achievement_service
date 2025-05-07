package faang.school.achievement.service;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.dto.AchievementProgressDto;
import faang.school.achievement.dto.UserAchievementDto;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.mapper.AchievementProgressMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.service.impl.AchievementRequestServiceImpl;

import faang.school.achievement.validator.AchievementPageableValidator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementRequestServiceImplUnitTest {

    private static final long USER_ID = 1L;
    private static final long ACHIEVEMENT_ID = 1L;

    @Mock
    private AchievementCache achievementCache;

    @Mock
    private AchievementMapper achievementMapper;

    @Mock
    private AchievementProgressMapper achievementProgressMapper;

    @Mock
    private AchievementPageableValidator pageableValidator;

    @InjectMocks
    private AchievementRequestServiceImpl achievementService;

    @Test
    void testGetAllAchievements() {
        String title = "John";
        String description = "Test description";
        Rarity rarity = Rarity.UNCOMMON;

        AchievementFilterDto filterDto = AchievementFilterDto.builder()
                .title(title)
                .description(description)
                .rarity(rarity)
                .page(1)
                .size(10)
                .build();

        List<Achievement> achievements = List.of(new Achievement());
        List<AchievementDto> achievementsDto = List.of(new AchievementDto());

        doNothing().when(pageableValidator).validateAndSetDefaults(filterDto);

        when(achievementCache.findFilteredAchievements(eq(title), eq(description), eq(rarity), any(Pageable.class)))
                .thenReturn(achievements);
        when(achievementMapper.toDtoList(achievements)).thenReturn(achievementsDto);

        List<AchievementDto> result = achievementService.getAllAchievements(filterDto);

        assertNotNull(result);
        assertEquals(achievementsDto, result);
        verify(pageableValidator).validateAndSetDefaults(filterDto);
        verify(achievementCache).findFilteredAchievements(eq(title), eq(description), eq(rarity), any(Pageable.class));
        verify(achievementMapper).toDtoList(achievements);
    }

    @Test
    void testGetAllAchievementsReturnsEmptyList() {
        String title = "John";
        String description = "Test description";
        Rarity rarity = Rarity.LEGENDARY;

        AchievementFilterDto filterDto = AchievementFilterDto.builder()
                .title(title)
                .description(description)
                .rarity(rarity)
                .page(1)
                .size(20)
                .build();

        doNothing().when(pageableValidator).validateAndSetDefaults(filterDto);

        when(achievementCache.findFilteredAchievements(eq(title), eq(description), eq(rarity), any(Pageable.class)))
                .thenReturn(List.of());
        when(achievementMapper.toDtoList(List.of())).thenReturn(List.of());

        List<AchievementDto> result = achievementService.getAllAchievements(filterDto);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(pageableValidator).validateAndSetDefaults(filterDto);
        verify(achievementCache).findFilteredAchievements(eq(title), eq(description), eq(rarity), any(Pageable.class));
        verify(achievementMapper).toDtoList(List.of());
    }

    @Test
    void testGetUserAchievements() {
        List<UserAchievement> userAchievements = List.of(new UserAchievement());
        List<UserAchievementDto> userAchievementsDto = List.of(new UserAchievementDto());
        when(achievementCache.getUserAchievements(USER_ID)).thenReturn(userAchievements);
        when(achievementMapper.toUserAchievementDtoList(userAchievements)).thenReturn(userAchievementsDto);

        List<UserAchievementDto> result = achievementService.getUserAchievements(USER_ID);

        assertNotNull(result);
        assertEquals(userAchievementsDto, result);
        verify(achievementCache, times(1)).getUserAchievements(USER_ID);
        verify(achievementMapper, times(1)).toUserAchievementDtoList(userAchievements);
    }

    @Test
    void testGetUserAchievementsReturnsEmptyList() {
        when(achievementCache.getUserAchievements(USER_ID)).thenReturn(List.of());
        when(achievementMapper.toUserAchievementDtoList(List.of())).thenReturn(List.of());

        List<UserAchievementDto> result = achievementService.getUserAchievements(USER_ID);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(achievementCache).getUserAchievements(USER_ID);
        verify(achievementMapper).toUserAchievementDtoList(List.of());
    }

    @Test
    void testGetExistedAchievementById() {
        Achievement achievement = new Achievement();
        AchievementDto achievementDto = new AchievementDto();

        when(achievementCache.getAchievementById(ACHIEVEMENT_ID)).thenReturn(achievement);
        when(achievementMapper.toDto(achievement)).thenReturn(achievementDto);

        AchievementDto result = achievementService.getAchievementById(ACHIEVEMENT_ID);

        assertNotNull(result);
        assertEquals(achievementDto, result);
        verify(achievementCache, times(1)).getAchievementById(ACHIEVEMENT_ID);
        verify(achievementMapper, times(1)).toDto(achievement);
    }

    @Test
    void testGetNotExistedAchievementById() {
        when(achievementCache.getAchievementById(ACHIEVEMENT_ID)).thenThrow(
                new EntityNotFoundException("Achievement not found"));
        assertThrows(EntityNotFoundException.class, () -> achievementService.getAchievementById(ACHIEVEMENT_ID));
        verify(achievementCache, times(1)).getAchievementById(ACHIEVEMENT_ID);
    }

    @Test
    void testGetUserUnearnedAchievements() {
        List<AchievementProgress> achievements = List.of(new AchievementProgress());
        List<AchievementProgressDto> achievementsDto = List.of(new AchievementProgressDto());

        when(achievementCache.getUserUnearnedAchievements(USER_ID)).thenReturn(achievements);
        when(achievementProgressMapper.toDtoList(achievements)).thenReturn(achievementsDto);

        List<AchievementProgressDto> result = achievementService.getUserUnearnedAchievements(USER_ID);

        assertNotNull(result);
        assertEquals(achievementsDto, result);
        verify(achievementCache, times(1)).getUserUnearnedAchievements(USER_ID);
        verify(achievementProgressMapper, times(1)).toDtoList(achievements);
    }

    @Test
    void testGetUserUnearnedAchievementsReturnsEmptyList() {
        when(achievementCache.getUserUnearnedAchievements(USER_ID)).thenReturn(List.of());
        when(achievementProgressMapper.toDtoList(List.of())).thenReturn(List.of());

        List<AchievementProgressDto> result = achievementService.getUserUnearnedAchievements(USER_ID);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(achievementCache).getUserUnearnedAchievements(USER_ID);
        verify(achievementProgressMapper).toDtoList(List.of());
    }
}

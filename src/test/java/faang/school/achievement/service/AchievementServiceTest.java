package faang.school.achievement.service;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.exception.ResourceNotFoundException;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static faang.school.achievement.model.Rarity.RARE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        doNothing().when(achievementCache).addToCache(eq(achievementDto));

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
}
package faang.school.achievement.service;

import faang.school.achievement.cache.AchievementCache;
import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.handler.EntityHandler;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {
    @InjectMocks
    private AchievementService achievementService;
    @Mock
    private EntityHandler entityHandler;
    @Mock
    private AchievementCache achievementCache;
    @Mock
    private AchievementMapper achievementMapper;

    private Achievement achievement;
    private AchievementDto achievementDto;
    private String achievementTitle;

    @BeforeEach
    public void setUp() {
        achievementTitle = "achievementTitle";
        achievement = Achievement.builder()
                .title(achievementTitle).build();
        achievementDto = AchievementDto.builder()
                .title(achievementTitle).build();

    }

    @Nested
    @DisplayName("Method: getAchievementByTitle")
    class testGetAchievement {
        @Test
        @DisplayName("testing getAchievementByTitle with getting achievement from cache")
        void testGetAchievementByTitleGetFromCache() {
            when(achievementCache.get(achievementTitle)).thenReturn(Optional.ofNullable(achievement));
            when(achievementMapper.toDto(achievement)).thenReturn(achievementDto);

            AchievementDto achievementByTitle = achievementService.getAchievementByTitle(achievementTitle);

            verify(achievementCache, times(1)).get(achievementTitle);
            verify(achievementMapper, times(1)).toDto(achievement);
            assertEquals(achievementDto, achievementByTitle);
        }

        @Test
        @DisplayName("testing getAchievementByTitle with getting achievement from database")
        void testGetAchievementByTitleGetFromDatabase() {
            when(achievementCache.get(achievementTitle)).thenReturn(Optional.empty());
            when(entityHandler.getOrThrowException(eq(Achievement.class), eq(achievementTitle), any())).thenReturn(achievement);
            when(achievementMapper.toDto(achievement)).thenReturn(achievementDto);

            AchievementDto achievementByTitle = achievementService.getAchievementByTitle(achievementTitle);

            verify(achievementCache, times(1)).get(achievementTitle);
            verify(entityHandler, times(1))
                    .getOrThrowException(eq(Achievement.class), eq(achievementTitle), any());
            verify(achievementMapper, times(1)).toDto(achievement);
            assertEquals(achievementDto, achievementByTitle);
        }
    }
}
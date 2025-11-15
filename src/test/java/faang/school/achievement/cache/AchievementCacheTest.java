package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.repository.AchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementCacheTest {
    @Mock
    private AchievementRepository achievementRepository;
    @InjectMocks
    private AchievementCache achievementCache;

    @BeforeEach
    public void setUp() {
        Achievement achievement = Achievement.builder()
                .id(1L)
                .title("COLLECTOR")
                .description("For 100 goals")
                .rarity(Rarity.COMMON)
                .points(15L)
                .createdAt(LocalDateTime.parse("2025-11-10T18:28:15.759969"))
                .updatedAt(LocalDateTime.parse("2025-11-10T18:28:15.759969"))
                .build();

        when(achievementRepository.findAll()).thenReturn(Arrays.asList(achievement));
        achievementCache.init();
    }

    @Test
    public void init_ShouldLoadAchievementsIntoCache() {
        Achievement cached = achievementCache.get("COLLECTOR");

        assertEquals("COLLECTOR", cached.getTitle());
        assertEquals("For 100 goals", cached.getDescription());
        assertNotNull(cached);
        verify(achievementRepository, times(1)).findAll();
    }

    @Test
    public void get_ShouldReturnNullForNonExistentTitle() {
        Achievement cached = achievementCache.get("unknown");

        assertNull(cached);
    }

    @Test
    public void get_ShouldReturnCorrectAchievement() {
        Achievement result = achievementCache.get("COLLECTOR");

        assertNotNull(result);
        assertEquals("COLLECTOR", result.getTitle());
        assertEquals("For 100 goals", result.getDescription());
        assertEquals(Rarity.COMMON, result.getRarity());
        assertEquals(15L, result.getPoints());
    }
}

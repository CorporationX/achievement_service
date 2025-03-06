package faang.school.achievement.cache;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.repository.AchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AchievementCacheTest {

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @Spy
    private AchievementMapper achievementMapper = Mappers.getMapper(AchievementMapper.class);

    private AchievementCache achievementCache;

    @BeforeEach
    void setUp() {
        lenient().when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        achievementCache = new AchievementCache(achievementRepository, redisTemplate, achievementMapper);
        ReflectionTestUtils.setField(achievementCache, "achievementsKey", "achievements");
    }

    @Test
    void refreshCache_HandlesDuplicates() {
        Achievement achievement1 = Achievement.builder()
                .id(1L)
                .title("Title1")
                .description("Description1")
                .rarity(Rarity.COMMON)
                .points(10)
                .build();
        Achievement achievement2 = Achievement.builder()
                .id(2L)
                .title("Title1")
                .description("Another Description")
                .rarity(Rarity.RARE)
                .points(20)
                .build();
        List<Achievement> achievements = Arrays.asList(achievement1, achievement2);
        when(achievementRepository.findAll()).thenReturn(achievements);

        achievementCache.refreshCache();
        ArgumentCaptor<Map<String, AchievementDto>> captor = ArgumentCaptor.forClass(Map.class);
        verify(hashOperations).putAll(eq("achievements"), captor.capture());
        Map<String, AchievementDto> achievementsMap = captor.getValue();

        assertEquals(1, achievementsMap.size());
        assertTrue(achievementsMap.containsKey("Title1"));

        AchievementDto expectedDto = achievementMapper.achievementToAchievementDTO(achievement1);
        assertEquals(expectedDto, achievementsMap.get("Title1"));
    }

    @Test
    void getAchievement_ReturnsValue() {
        String title = "TestTitle";
        AchievementDto dto = new AchievementDto(1L, title, "Some description", Rarity.COMMON, 10);
        when(hashOperations.get("achievements", title)).thenReturn(dto);

        AchievementDto result = achievementCache.getAchievement(title);
        assertNotNull(result);
        assertEquals(dto, result);
    }

    @Test
    void getAchievement_WithInvalidTitle() {
        assertThrows(IllegalArgumentException.class, () -> achievementCache.getAchievement(null));
        assertThrows(IllegalArgumentException.class, () -> achievementCache.getAchievement("   "));
    }
}

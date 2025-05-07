package faang.school.achievement.service.cache;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.exception.AchievementNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test cases of DefaultAchievementCacheServiceTest")
public class DefaultAchievementCacheServiceTest {

    private static final String ACHIEVEMENT_TITLE = "Achievement";

    @Mock
    private RedisTemplate<String, AchievementDto> redisTemplate;

    @Mock
    private ValueOperations<String, AchievementDto> valueOperations;

    @Mock
    private AchievementKeyGenerator keyGenerator;

    @InjectMocks
    private DefaultAchievementCacheService cacheService;

    @BeforeEach
    public void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("getAchievement - achievement not found")
    public void testGetAchievementWithoutAchievement() {
        String expectedMessage = String.format("Achievement \"%s\" not found in Redis", ACHIEVEMENT_TITLE);

        Exception exception = assertThrows(AchievementNotFoundException.class,
                () -> cacheService.getAchievement(ACHIEVEMENT_TITLE));

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("getAchievement - successfully")
    public void testGetAchievementSuccessfully() {
        String achievementKey = String.format("achievement:%s", ACHIEVEMENT_TITLE);
        AchievementDto achievement = AchievementDto.builder().title(ACHIEVEMENT_TITLE).build();

        when(keyGenerator.createAchievementKey(ACHIEVEMENT_TITLE)).thenReturn(achievementKey);
        when(valueOperations.get(achievementKey)).thenReturn(achievement);

        AchievementDto actualAchievement = cacheService.getAchievement(ACHIEVEMENT_TITLE);

        verify(keyGenerator, times(1)).createAchievementKey(ACHIEVEMENT_TITLE);
        verify(redisTemplate, times(1)).opsForValue();
        verify(valueOperations, times(1)).get(achievementKey);
        assertNotNull(actualAchievement);
        assertEquals(achievement, actualAchievement);
    }
}

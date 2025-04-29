package faang.school.achievement.service.cache;

import faang.school.achievement.dto.AchievementDto;
import faang.school.achievement.service.achievement.AchievementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test cases of AchievementCacheInitializerTest")
public class AchievementCacheInitializerTest {

    @Mock
    private AchievementService achievementService;

    @Mock
    private RedisTemplate<String, AchievementDto> redisTemplate;

    @Mock
    private ValueOperations<String, AchievementDto> valueOperations;

    @Mock
    private AchievementKeyGenerator keyGenerator;

    @InjectMocks
    private AchievementCacheInitializer cacheInitializer;

    @Test
    @DisplayName("loadAllAchievementsIntoCache - empty achievement list")
    public void testLoadAllAchievementsIntoCacheWithEmptyList() {
        when(achievementService.getAchievements()).thenReturn(List.of());

        cacheInitializer.loadAllAchievementsIntoCache();

        verify(achievementService, times(1)).getAchievements();
        verifyNoInteractions(redisTemplate, keyGenerator);
    }

    @Test
    @DisplayName("loadAllAchievementsIntoCache - successfully")
    public void testLoadAllAchievementsIntoCacheSuccessfully() {
        AchievementDto firstAchievement = AchievementDto.builder()
                .title("First")
                .build();
        AchievementDto secondAchievement = AchievementDto.builder()
                .title("Second")
                .build();

        when(achievementService.getAchievements()).thenReturn(List.of(firstAchievement, secondAchievement));
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(keyGenerator.createAchievementKey(anyString()))
                .thenAnswer(invocation -> "achievement:" + invocation.getArgument(0));

        cacheInitializer.loadAllAchievementsIntoCache();

        verify(valueOperations, times(1)).set("achievement:First", firstAchievement);
        verify(valueOperations, times(1)).set("achievement:Second", secondAchievement);
    }
}

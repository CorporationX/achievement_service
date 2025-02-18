package faang.school.achievement.cache;

import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AchievementCacheTest {

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @InjectMocks
    private AchievementCache achievementCache;


    @Test
    public void testGetAchievementFromRepo() {
        Achievement expected = Achievement.builder().title("Test").build();

        Mockito.when(achievementRepository.findByTitle("Test")).thenReturn(Optional.of(expected));
        Achievement result = achievementCache.get("Test");

        assertEquals(expected, result);
    }

    @Test
    public void testGetNotExistAchievement() {
        Mockito.when(achievementRepository.findByTitle("Test")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> achievementCache.get("Test"));
    }

    @Test
    public void testAchievementCacheWarmUp() throws Exception {
        Achievement achievement = Achievement.builder().title("Test").build();
        Mockito.when(cacheManager.getCache("achievementTitle")).thenReturn(cache);
        Mockito.when(achievementRepository.findAll()).thenReturn(List.of(achievement));

        achievementCache.warmUpCache();

        Mockito.verify(cache).put("Test", achievement);
    }
}

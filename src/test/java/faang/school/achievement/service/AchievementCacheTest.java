package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.service.cache.AchievementCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class AchievementCacheTest {

    @Autowired
    private AchievementCache achievementCache;

    @MockBean
    private AchievementRepository achievementRepository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setup() {
        Objects.requireNonNull(cacheManager.getCache("achievements")).clear();
    }

    @Test
    @DisplayName("Should initialize cache on startup with all achievements from the database")
    void testCacheInitializationOnStartup() {
        Achievement achievement1 = Achievement.builder()
                .title("First")
                .description("Desc1")
                .build();

        Achievement achievement2 = Achievement.builder()
                .title("Second")
                .description("Desc2")
                .build();

        when(achievementRepository.findAll()).thenReturn(List.of(achievement1, achievement2));

        achievementCache.initCache();

        Cache cache = cacheManager.getCache("achievements");
        assert cache != null;
        assertNotNull(cache.get("First"), "Achievement 'First' should be cached on initialization.");
        assertNotNull(cache.get("Second"), "Achievement 'Second' should be cached on initialization.");
    }

    @Test
    @DisplayName("Should retrieve an achievement from the database if it is not in cache")
    void testGet_CacheMiss() {
        Achievement mockAchievement = Achievement.builder()
                .title("First")
                .description("Desc")
                .build();

        when(achievementRepository.findByTitle("First")).thenReturn(Optional.of(mockAchievement));

        Achievement result = achievementCache.get("First");

        assertEquals("First", result.getTitle(), "Retrieved achievement should have title 'First'.");
        verify(achievementRepository, times(1)).findByTitle("First");
    }

    @Test
    @DisplayName("Should add or update an achievement in cache and save it in the database")
    void testAddOrUpdate_UpdatesCache() {
        Achievement newAchievement = Achievement.builder()
                .title("New")
                .description("Desc")
                .build();

        when(achievementRepository.save(newAchievement)).thenReturn(newAchievement);

        Achievement result = achievementCache.addOrUpdate(newAchievement);

        Cache cache = cacheManager.getCache("achievements");
        assert cache != null;
        assertNotNull(cache.get("New"), "Achievement 'New' should be stored in cache after adding/updating.");
    }

    @Test
    @DisplayName("Should remove an achievement from both cache and database when deleted")
    void testRemove_EvictsFromCache() {
        Cache cache = cacheManager.getCache("achievements");
        assert cache != null;
        cache.put("ToDelete", Achievement.builder()
                .title("ToDelete")
                .description("Desc")
                .build());

        achievementCache.remove("ToDelete");

        assertNull(cache.get("ToDelete"), "Achievement 'ToDelete' should be removed from cache.");
        verify(achievementRepository, times(1)).deleteByTitle("ToDelete");
    }
}

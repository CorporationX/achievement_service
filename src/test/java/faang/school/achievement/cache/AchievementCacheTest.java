package faang.school.achievement.cache;

import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.redis.cache.AchievementCacheImpl;
import faang.school.achievement.repository.AchievementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementCacheTest {
    private static final long ID = 1L;
    private static final String DEFAULT_ACHIEVEMENT_TITLE = "test";

    @InjectMocks
    private AchievementCacheImpl cache;
    @Mock
    private AchievementRepository repository;


    @Test
    void testGetWhenNotInCache() {
        Achievement found = makeAchievement(ID, DEFAULT_ACHIEVEMENT_TITLE);
        when(repository.findByTitleOrThrow(DEFAULT_ACHIEVEMENT_TITLE)).thenReturn(found);

        Achievement result = cache.get(DEFAULT_ACHIEVEMENT_TITLE);

        assertEquals(found, result);
        verify(repository, times(1)).findByTitleOrThrow(DEFAULT_ACHIEVEMENT_TITLE);
    }

    @Test
    void testWhenPresentInCache() {
        Achievement existing = makeAchievement(ID, DEFAULT_ACHIEVEMENT_TITLE);

        cache.put(existing);
        Achievement got = cache.get(DEFAULT_ACHIEVEMENT_TITLE);

        assertEquals(existing, got);
        verify(repository, never()).findByTitleOrThrow(DEFAULT_ACHIEVEMENT_TITLE);
    }

    @Test
    void testGetNotExistedAchievement() {
        when(repository.findByTitleOrThrow(DEFAULT_ACHIEVEMENT_TITLE)).thenThrow(new EntityNotFoundException("not found"));

        assertThrows(EntityNotFoundException.class, () -> cache.get(DEFAULT_ACHIEVEMENT_TITLE));
    }

    @Test
    void testFlushMethod() {
        Achievement achievement = makeAchievement(ID, DEFAULT_ACHIEVEMENT_TITLE);

        cache.put(achievement);

        Map<String, Achievement> cachedAchievements = cache.getAll();

        assertEquals(1, cachedAchievements.size());

        cachedAchievements.remove(DEFAULT_ACHIEVEMENT_TITLE);

        Map<String, Achievement> afterMutation = cache.getAll();

        assertEquals(1, afterMutation.size());

        cache.flush();

        assertEquals(0, cache.getAll().size());
    }

    private Achievement makeAchievement(long id, String title) {
        return Achievement.builder()
                .id(id)
                .title(title)
                .build();
    }

}

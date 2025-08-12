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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementCacheTest {
    private static final long ACHIEVEMENT_ID = 1;
    private static final String DEFAULT_ACHIEVEMENT_TITLE = "test";

    @InjectMocks
    private AchievementCacheImpl cache;
    @Mock
    private AchievementRepository repository;


    @Test
    void testGetWhenNotInCache() {
        Achievement found = makeAchievement(ACHIEVEMENT_ID, DEFAULT_ACHIEVEMENT_TITLE);
        when(repository.findByIdOrThrow(ACHIEVEMENT_ID)).thenReturn(found);

        Achievement result = cache.get(ACHIEVEMENT_ID);

        assertEquals(found, result);
        verify(repository, times(1)).findByIdOrThrow(ACHIEVEMENT_ID);
    }

    @Test
    void testWhenPresentInCache() {
        Achievement existing = makeAchievement(ACHIEVEMENT_ID, DEFAULT_ACHIEVEMENT_TITLE);

        cache.put(existing);
        Achievement got = cache.get(ACHIEVEMENT_ID);

        assertEquals(existing, got);
        verify(repository, never()).findByIdOrThrow(anyLong());
    }

    @Test
    void testGetNotExistedAchievement() {
        when(repository.findByIdOrThrow(ACHIEVEMENT_ID)).thenThrow(new EntityNotFoundException("not found"));

        assertThrows(EntityNotFoundException.class, () -> cache.get(ACHIEVEMENT_ID));
    }

    @Test
    void put_and_flush_and_getAll_behaviour() {
        Achievement achievement = makeAchievement(ACHIEVEMENT_ID, DEFAULT_ACHIEVEMENT_TITLE);

        cache.put(achievement);

        Map<Long, Achievement> cachedAchievements = cache.getAll();

        assertEquals(1, cachedAchievements.size());

        cachedAchievements.remove(ACHIEVEMENT_ID);

        Map<Long, Achievement> afterMutation = cache.getAll();

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

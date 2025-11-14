package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.service.DataForTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementCacheTest extends DataForTests {
    @Mock
    private AchievementRepository achievementRepository;
    @InjectMocks
    private AchievementCache achievementCache;

    @BeforeEach
    void setUp() {
        when(achievementRepository.findAll()).thenReturn(allAchievements);
        achievementCache.reloadCache();
    }

    @Test
    void getByTitle_shouldReturnAchievementWhenExists() {
        Optional<Achievement> result = achievementCache.getByTitle(ACHIEVEMENT_TITLE_COLLECTOR);

        assertTrue(result.isPresent());
        assertEquals(ACHIEVEMENT_ID_1, result.get().getId());
        assertEquals(ACHIEVEMENT_TITLE_COLLECTOR, result.get().getTitle());
    }

    @Test
    void getByTitle_shouldReturnEmptyWhenNotExists() {
        Optional<Achievement> result = achievementCache.getByTitle("Non Existent");

        assertFalse(result.isPresent());
    }

    @Test
    void getById_shouldReturnAchievementWhenExists() {
        Optional<Achievement> result = achievementCache.getById(ACHIEVEMENT_ID_1);

        assertTrue(result.isPresent());
        assertEquals(ACHIEVEMENT_ID_1, result.get().getId());
        assertEquals(ACHIEVEMENT_TITLE_COLLECTOR, result.get().getTitle());
    }

    @Test
    void getById_shouldReturnEmptyWhenNotExists() {
        Optional<Achievement> result = achievementCache.getById(UNKNOWN_ID);

        assertFalse(result.isPresent());
    }

    @Test
    void getAll_shouldReturnAllAchievements() {
        List<Achievement> achievementActual = achievementCache.getAll();

        assertNotNull(achievementActual);
        assertEquals(8, achievementActual.size());
        assertEquals(new HashSet<>(allAchievements),
                new HashSet<>(achievementActual));
    }

    @Test
    void reloadCache_shouldUpdateCacheWithNewData() {

        when(achievementRepository.findAll()).thenReturn(allAchievements);
        achievementCache.reloadCache();

        List<Achievement> achievementActual = achievementCache.getAll();
        assertEquals(8, achievementActual.size());
        assertEquals(new HashSet<>(allAchievements),
                new HashSet<>(achievementActual));
    }

    @Test
    void reloadCache_shouldReplaceOldCache() {
        Achievement newAchievement = Achievement.builder()
                .id(10L)
                .title(UNKNOWN_ACHIEVEMENT_TITLE)
                .build();

        when(achievementRepository.findAll()).thenReturn(List.of(newAchievement));
        achievementCache.reloadCache();

        List<Achievement> result = achievementCache.getAll();
        assertEquals(1, result.size());
        assertFalse(achievementCache.getByTitle(ACHIEVEMENT_TITLE_COLLECTOR).isPresent());
        assertTrue(achievementCache.getByTitle(UNKNOWN_ACHIEVEMENT_TITLE).isPresent());
    }

    @Test
    void reloadCache_shouldBeCalledOnce() {
        verify(achievementRepository).findAll();
    }

    @Test
    void getById_shouldNotQueryRepositoryAfterCacheInit() {
        clearInvocations(achievementRepository);

        achievementCache.getById(ACHIEVEMENT_ID_1);
        achievementCache.getById(ACHIEVEMENT_ID_2);

        verify(achievementRepository, never()).findAll();
        verify(achievementRepository, never()).findById(anyLong());
    }

    @Test
    void testGetByTitle_shouldNotQueryRepositoryAfterCacheInit() {
        clearInvocations(achievementRepository);

        achievementCache.getByTitle(ACHIEVEMENT_TITLE_COLLECTOR);
        achievementCache.getByTitle(ACHIEVEMENT_TITLE_EXPERT);

        verify(achievementRepository, never()).findAll();
    }
}
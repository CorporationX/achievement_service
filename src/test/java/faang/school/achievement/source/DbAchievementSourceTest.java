package faang.school.achievement.source;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.service.DataForTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DbAchievementSourceTest extends DataForTests {

    @Mock
    private AchievementRepository achievementRepository;

    @InjectMocks
    private DbAchievementSource dbAchievementSource;

    @Test
    void getAll_shouldReturnAllAchievements() {
        when(achievementRepository.findAll()).thenReturn(allAchievements);

        List<Achievement> achievementActual = dbAchievementSource.getAll();

        assertNotNull(achievementActual);
        assertEquals(8, achievementActual.size());
        assertEquals(new HashSet<>(allAchievements),
                new HashSet<>(achievementActual));
        verify(achievementRepository, times(1)).findAll();
    }

    @Test
    void getAll_shouldReturnEmptyListWhenNoAchievements() {
        when(achievementRepository.findAll()).thenReturn(Collections.emptyList());

        List<Achievement> result = dbAchievementSource.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(achievementRepository, times(1)).findAll();
    }

    @Test
    void getById_shouldReturnAchievementWhenExists() {
        when(achievementRepository.findById(ACHIEVEMENT_ID_1)).thenReturn(Optional.of(achievementCollector));

        Optional<Achievement> result = dbAchievementSource.getById(ACHIEVEMENT_ID_1);

        assertTrue(result.isPresent());
        assertEquals(ACHIEVEMENT_ID_1, result.get().getId());
        assertEquals(ACHIEVEMENT_TITLE_COLLECTOR, result.get().getTitle());
        verify(achievementRepository, times(1)).findById(ACHIEVEMENT_ID_1);
    }

    @Test
    void getById_shouldReturnEmptyIfRepositoryIsEmpty() {
        when(achievementRepository.findById(UNKNOWN_ID)).thenReturn(Optional.empty());

        Optional<Achievement> result = dbAchievementSource.getById(UNKNOWN_ID);

        assertFalse(result.isPresent());
        verify(achievementRepository, times(1)).findById(UNKNOWN_ID);
    }

    @Test
    void getAll_shouldCallRepositoryOnce() {
        when(achievementRepository.findAll()).thenReturn(allAchievements);

        dbAchievementSource.getAll();

        verify(achievementRepository, times(1)).findAll();
        verifyNoMoreInteractions(achievementRepository);
    }

    @Test
    void getByTitle_whenAchievementDoesNotExistShouldReturnEmptyOptional() {

        when(achievementRepository.findByTitle(UNKNOWN_ACHIEVEMENT_TITLE))
                .thenReturn(Optional.empty());

        Optional<Achievement> result = dbAchievementSource.getByTitle(UNKNOWN_ACHIEVEMENT_TITLE);

        assertFalse(result.isPresent());

        verify(achievementRepository, times(1)).findByTitle(UNKNOWN_ACHIEVEMENT_TITLE);
    }


    @Test
    void getByTitle_withDifferentTitlesShouldReturnDifferentResults() {

        when(achievementRepository.findByTitle(ACHIEVEMENT_TITLE_COLLECTOR))
                .thenReturn(Optional.of(achievementCollector));
        when(achievementRepository.findByTitle(ACHIEVEMENT_TITLE_EXPERT))
                .thenReturn(Optional.of(achievementExpert));

        Optional<Achievement> result1 = dbAchievementSource.getByTitle(ACHIEVEMENT_TITLE_COLLECTOR);
        Optional<Achievement> result2 = dbAchievementSource.getByTitle(ACHIEVEMENT_TITLE_EXPERT);

        assertTrue(result1.isPresent());
        assertTrue(result2.isPresent());
        assertEquals(result1.get().getId(), ACHIEVEMENT_ID_1);
        assertEquals(result2.get().getId(), ACHIEVEMENT_ID_3);
        assertNotEquals(result1.get(), result2.get());
    }

    @Test
    void getByTitle_ShouldReturnSameOptionalOnMultipleCalls() {
        when(achievementRepository.findByTitle(ACHIEVEMENT_TITLE_COLLECTOR))
                .thenReturn(Optional.of(achievementCollector));

        Optional<Achievement> result1 = dbAchievementSource.getByTitle(ACHIEVEMENT_TITLE_COLLECTOR);
        Optional<Achievement> result2 = dbAchievementSource.getByTitle(ACHIEVEMENT_TITLE_COLLECTOR);

        assertTrue(result1.isPresent());
        assertTrue(result2.isPresent());
        assertEquals(result1.get(), result2.get());

        verify(achievementRepository, times(2)).findByTitle(ACHIEVEMENT_TITLE_COLLECTOR);
    }
}

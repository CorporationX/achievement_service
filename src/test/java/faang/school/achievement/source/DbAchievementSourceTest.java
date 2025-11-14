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
    void getByTitle_shouldReturnAchievementWhenExists() {
        when(achievementRepository.findAll()).thenReturn(allAchievements);

        Optional<Achievement> result = dbAchievementSource.getByTitle(ACHIEVEMENT_TITLE_COLLECTOR);

        assertTrue(result.isPresent());
        assertEquals(ACHIEVEMENT_ID_1, result.get().getId());
        assertEquals(ACHIEVEMENT_TITLE_COLLECTOR, result.get().getTitle());
        verify(achievementRepository, times(1)).findAll();
    }

    @Test
    void getByTitle_shouldReturnEmptyWhenNotExists() {
        when(achievementRepository.findAll()).thenReturn(allAchievements);

        Optional<Achievement> result = dbAchievementSource.getByTitle(UNKNOWN_ACHIEVEMENT_TITLE);

        assertFalse(result.isPresent());
        verify(achievementRepository, times(1)).findAll();
    }

    @Test
    void getByTitle_shouldReturnEmptyWhenRepositoryIsEmpty() {
        when(achievementRepository.findAll()).thenReturn(Collections.emptyList());

        Optional<Achievement> result = dbAchievementSource.getByTitle(ACHIEVEMENT_TITLE_COLLECTOR);

        assertFalse(result.isPresent());
        verify(achievementRepository, times(1)).findAll();
    }

    @Test
    void getByTitle_shouldHandleNullTitleCorrectly() {
        when(achievementRepository.findAll()).thenReturn(allAchievements);

        // Act
        Optional<Achievement> result = dbAchievementSource.getByTitle(null);

        // Assert
        assertFalse(result.isPresent());
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
    void getByTitle_shouldCallGetAllMethod() {
        when(achievementRepository.findAll()).thenReturn(allAchievements);

        dbAchievementSource.getByTitle("First Achievement");

        verify(achievementRepository, times(1)).findAll();
    }
}

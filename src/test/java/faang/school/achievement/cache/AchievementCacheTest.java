package faang.school.achievement.cache;

import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class AchievementCacheTest {

    @InjectMocks
    private AchievementCache achievementCache;

    @Mock
    private AchievementRepository achievementRepository;

    private Achievement achievement;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
         achievement = Achievement.builder()
                .title("Title")
                .build();
    }

    @Test
    public void testInit() {
        when(achievementRepository.findAll()).thenReturn(List.of(achievement));

        achievementCache.init();

        assertEquals(achievement, achievementCache.get("Title"));
        Mockito.verify(achievementRepository).findAll();
    }

    @Test
    public void testGetExistingAchievement() {
        when(achievementRepository.findAll()).thenReturn(List.of(achievement));
        achievementCache.init();

        Achievement result = achievementCache.get("Title");

        assertEquals(achievement, result);
    }

    @Test
    public void testGetNonExistingAchievement() {
        when(achievementRepository.findAll()).thenReturn(List.of(achievement));
        achievementCache.init();

        assertThrows(EntityNotFoundException.class, () -> achievementCache.get("NonExistingTitle"));
    }
}
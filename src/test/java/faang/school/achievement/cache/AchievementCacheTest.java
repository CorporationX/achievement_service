package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementCacheTest {

    @Mock
    private AchievementRepository achievementRepository;

    @InjectMocks
    private AchievementCache achievementCache;

    private Achievement achievement1;
    private Achievement achievement2;

    @BeforeEach
    public void setUp() {
        achievement1 = new Achievement();
        achievement1.setTitle("Achievement 1");

        achievement2 = new Achievement();
        achievement2.setTitle("Achievement 2");

        when(achievementRepository.findAll()).thenReturn(Arrays.asList(achievement1, achievement2));

        achievementCache.fillAchievements();
    }

    @Test
    @DisplayName("Check filled achievements")
    public void testFillAchievements() {
        assertEquals(achievement1, achievementCache.get("Achievement 1"));
        assertEquals(achievement2, achievementCache.get("Achievement 2"));
    }

    @Test
    @DisplayName("Check get existing achievement")
    public void testGetAchievementExists() {
        Achievement achievement = achievementCache.get("Achievement 1");
        assertEquals("Achievement 1", achievement.getTitle());
    }

    @Test
    @DisplayName("Check get not existing achievement")
    public void testGetAchievementDoesNotExist() {
        Achievement achievement = achievementCache.get("Non-existing Achievement");
        assertNull(achievement);
    }
}
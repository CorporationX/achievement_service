package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AchievementCacheTest {

    @InjectMocks
    private AchievementCache achievementCache;

    @Mock
    private AchievementRepository achievementRepository;


    @Test
    void get() {
        Achievement achievement = new Achievement();
        achievement.setTitle("Achievement");

        when(achievementRepository.findAll()).thenReturn(List.of(achievement));

        achievementCache.init();
        Achievement testAchievement = achievementCache.get("Achievement");

        assertEquals(achievement.getTitle(), testAchievement.getTitle());
        assertNotNull(testAchievement);
        verify(achievementRepository, times(1)).findAll();
    }

    @Test
    void testCacheInitialization() {
        Achievement achievement1 = new Achievement();
        achievement1.setTitle("Achievement1");

        Achievement achievement2 = new Achievement();
        achievement2.setTitle("Achievement2");

        List<Achievement> achievements = List.of(achievement1, achievement2);

        when(achievementRepository.findAll()).thenReturn(achievements);

        achievementCache.init();

        Achievement testAchievement = achievementCache.get("Achievement1");
        assertNotNull(testAchievement);
        assertEquals("Achievement1", testAchievement.getTitle());

        testAchievement = achievementCache.get("Achievement2");
        assertNotNull(testAchievement);
        assertEquals("Achievement2", testAchievement.getTitle());

        verify(achievementRepository, times(1)).findAll();
    }
}
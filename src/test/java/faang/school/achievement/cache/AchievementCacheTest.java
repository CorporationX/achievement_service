package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(value = {MockitoExtension.class})
public class AchievementCacheTest {

    @Mock
    private AchievementRepository achievementRepository;

    @InjectMocks
    private AchievementCache achievementCache;

    private Achievement achievementFirst = new Achievement();
    private Achievement achievementSecond = new Achievement();
    private List<Achievement> achievements = new ArrayList<>();

    @BeforeEach
    void setUp() {
        achievementFirst.setTitle("title1");
        achievementSecond.setTitle("title2");
        achievements.add(achievementFirst);
        achievements.add(achievementSecond);
        when(achievementRepository.findAll()).thenReturn(achievements);
    }

    @Test
    public void testInitialization() {
        achievementCache.initialization();
        verify(achievementRepository, times(1)).findAll();
    }

    @Test
    public void testGet() {
        Achievement achievementTest = new Achievement();
        achievementTest.setTitle("title1");
        System.out.println(achievements);
        achievementCache.initialization();
        Achievement result = achievementCache.get("title1");
        System.out.println(result);
        assertEquals(achievementTest, result);
    }

}

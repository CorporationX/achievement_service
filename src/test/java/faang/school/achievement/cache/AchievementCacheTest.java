package faang.school.achievement.cache;

import faang.school.achievement.exception.EntityNotFoundException;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AchievementCacheTest {

    @Mock
    private AchievementRepository achievementRepository;

    @InjectMocks
    private AchievementCache achievementCache;

    @Test
    public void testGetAchievementFromCache() {
        Achievement expected = Achievement.builder().title("Test").build();
        ReflectionTestUtils.setField(achievementCache, "achievements", Map.of("Test", expected));

        Achievement result = achievementCache.get("Test");
        assertEquals(expected, result);
    }


    @Test
    public void testGetAchievementFromRepo() {
        Achievement expected = Achievement.builder().title("Test").build();
        ReflectionTestUtils.setField(achievementCache, "achievements", Collections.emptyMap());

        Mockito.when(achievementRepository.findByTitle("Test")).thenReturn(Optional.of(expected));
        Achievement result = achievementCache.get("Test");

        assertEquals(expected, result);
    }

    @Test
    public void testGetNotExistAchievement() {
        ReflectionTestUtils.setField(achievementCache, "achievements", Map.of());

        Mockito.when(achievementRepository.findByTitle("Test")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> achievementCache.get("Test"));
    }
}

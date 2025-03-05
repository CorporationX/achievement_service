package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementCacheTest {

    @Mock
    private AchievementRepository achievementRepository;

    @InjectMocks
    private AchievementCache achievementCache;

    private Achievement achievement;

    @BeforeEach
    void setUp() {
        achievement = Achievement.builder()
                .id(1L)
                .title("Achievement 1")
                .description("Description 1")
                .points(100)
                .build();
        Set<Achievement> achievements = Set.of(achievement);
        when(achievementRepository.findAllWithLazyCollections()).thenReturn(achievements);
        achievementCache.init();
    }

    @Test
    void testGet() {
        Achievement cachedAchievement = achievementCache.get("Achievement 1");
        assertThat(cachedAchievement)
                .isEqualTo(achievement);
    }

    @Test
    void testInit() {
        Achievement achievement2 = Achievement.builder()
                .id(2L)
                .title("Achievement 2")
                .description("Description 2")
                .points(200)
                .build();
        Set<Achievement> achievements = Set.of(achievement, achievement2);

        when(achievementRepository.findAllWithLazyCollections()).thenReturn(achievements);
        achievementCache.init();

        Achievement cachedAchievement1 = achievementCache.get("Achievement 1");
        Achievement cachedAchievement2 = achievementCache.get("Achievement 2");
        Set<Achievement> cachedAchievements = Set.of(cachedAchievement1, cachedAchievement2);

        assertThat(cachedAchievements)
                .isEqualTo(achievements);
    }
}
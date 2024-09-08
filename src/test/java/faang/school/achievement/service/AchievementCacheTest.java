package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.validator.AchievementCacheValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class AchievementCacheTest {

    @InjectMocks
    private AchievementCache achievementCache;

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private AchievementCacheValidator achievementCacheValidator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Achievement achievement1 = Achievement.builder()
                .id(1L)
                .title("Achievement 1")
                .description("Description 1")
                .build();

        Achievement achievement2 = Achievement.builder()
                .id(2L)
                .title("Achievement 2")
                .description("Description 2")
                .build();

        when(achievementRepository.findAll()).thenReturn(Arrays.asList(achievement1, achievement2));

        achievementCache.init();  // Инициализируем кэш вручную для теста
    }

    @Test
    public void testGetAchievement() {
        Achievement achievement = achievementCache.get("Achievement 1");
        assertEquals("Description 1", achievement.getDescription());
    }
}

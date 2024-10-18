package faang.school.achievement.config.cache;

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
import java.util.Collections;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementCacheTest {

    private static final String VALID_TITLE = "Title 1";
    private static final String NOT_VALID_TITLE = "123";
    @InjectMocks
    private AchievementCache achievementCache;
    @Mock
    private AchievementRepository achievementRepository;
    private Achievement achievement1;
    private Achievement achievement2;

    @BeforeEach
    void setUp() {
        achievement1 = Achievement.builder()
                .title(VALID_TITLE)
                .build();
        achievement2 = Achievement.builder()
                .title("def")
                .build();
    }

    @Test
    @DisplayName("Init cache of 2 achievements given by DB")
    public void whenInitMethodCalledThenFillUpCacheFromDB() {
        when(achievementRepository.findAll()).thenReturn(Arrays.asList(achievement1, achievement2));
        achievementCache.initCache();

        assertEquals(2, achievementCache.getCache().size());
        assertEquals(achievement1, achievementCache.getCache().get(achievement1.getTitle()));
        assertEquals(achievement2, achievementCache.getCache().get(achievement2.getTitle()));
    }

    @Test
    @DisplayName("When correct title passed return achievement by title")
    public void whenExistingTitlePassedThenReturnAchievement() {
        when(achievementRepository.findAll()).thenReturn(Collections.singletonList(achievement1));
        achievementCache.initCache();
        Achievement result = achievementCache.getAchievement(achievement1.getTitle());

        assertNotNull(result);
        assertEquals(achievement1.getTitle(), result.getTitle());
    }

    @Test
    @DisplayName("When not valid title passed then throw exception")
    public void whenNonExistingTitlePassedThenThrowException() {
        assertThrows(NoSuchElementException.class, () ->
                achievementCache.getAchievement(NOT_VALID_TITLE));
    }
}

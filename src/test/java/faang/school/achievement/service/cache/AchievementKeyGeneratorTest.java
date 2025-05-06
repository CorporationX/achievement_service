package faang.school.achievement.service.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test cases of AchievementKeyGeneratorTest")
public class AchievementKeyGeneratorTest {

    private AchievementKeyGenerator keyGenerator;

    @BeforeEach
    public void setUp() {
        keyGenerator = new AchievementKeyGenerator();
        ReflectionTestUtils.setField(keyGenerator, "keyPrefix", "achievement:");
    }

    @Test
    @DisplayName("createAchievementKey - lower case")
    public void testCreateAchievementKeyToLowerCase() {
        String actualKey = keyGenerator.createAchievementKey("ACHIEVEMENT");

        assertEquals("achievement:achievement", actualKey);
    }

    @Test
    @DisplayName("createAchievementKey - underscores")
    public void testCreateAchievementKeyWithUnderscores() {
        String actualKey = keyGenerator.createAchievementKey("test achievement");

        assertEquals("achievement:test_achievement", actualKey);
    }

    @Test
    @DisplayName("createAchievementKey - lower case + underscores")
    public void testCreateAchievementKeySuccessfully() {
        String actualKey = keyGenerator.createAchievementKey("TEST ACHIEVEMENT");

        assertEquals("achievement:test_achievement", actualKey);
    }
}

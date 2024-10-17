package faang.school.achievement.filter;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TitleFilterTest {

    private TitleFilter titleFilter;
    private AchievementFilterDto filter;
    private Achievement achievement1;
    private Achievement achievement2;

    @BeforeEach
    void setUp() {
        titleFilter = new TitleFilter();
        filter = new AchievementFilterDto();
        achievement1 = new Achievement();
        achievement2 = new Achievement();

        achievement1.setTitle("First Achievement");
        achievement2.setTitle("Second Achievement");
    }

    @Test
    void testIsAccepted_ShouldReturnTrueWhenTitleIsNotNullAndNotEmpty() {
        filter.setTitle("Test Title");

        boolean result = titleFilter.isAccepted(filter);

        assertTrue(result);
    }

    @Test
    void testIsAccepted_ShouldReturnFalseWhenTitleIsNull() {
        boolean result = titleFilter.isAccepted(filter);

        assertFalse(result);
    }

    @Test
    void testIsAccepted_ShouldReturnFalseWhenTitleIsEmpty() {
        filter.setTitle("");

        boolean result = titleFilter.isAccepted(filter);

        assertFalse(result);
    }

    @Test
    void testApply_ShouldFilterAchievementsByTitle() {
        List<Achievement> correctAnswer = List.of(achievement1);
        filter.setTitle("First Achievement");

        Stream<Achievement> achievementStream = Stream.of(achievement1, achievement2);
        List<Achievement> result = titleFilter.apply(achievementStream, filter).toList();

        assertEquals(correctAnswer, result);
    }

    @Test
    void testApply_ShouldReturnEmptyStreamWhenNoMatch() {
        filter.setTitle("Non-existent Achievement");

        Stream<Achievement> achievementStream = Stream.of(achievement1, achievement2);
        List<Achievement> result = titleFilter.apply(achievementStream, filter).toList();

        assertTrue(result.isEmpty());
    }

    @Test
    void testApply_ShouldBeCaseInsensitive() {
        List<Achievement> correctAnswer = List.of(achievement1);
        filter.setTitle("first achievement");

        Stream<Achievement> achievementStream = Stream.of(achievement1, achievement2);
        List<Achievement> result = titleFilter.apply(achievementStream, filter).toList();

        assertEquals(correctAnswer, result);
    }
}

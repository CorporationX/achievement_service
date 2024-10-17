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

class DescriptionFilterTest {

    private DescriptionFilter descriptionFilter;
    private AchievementFilterDto filter;
    private Achievement achievement1;
    private Achievement achievement2;

    @BeforeEach
    void setUp() {
        descriptionFilter = new DescriptionFilter();
        filter = new AchievementFilterDto();
        achievement1 = new Achievement();
        achievement2 = new Achievement();

        achievement1.setTitle("Test Achievement 1");
        achievement2.setTitle("Test Achievement 2");
    }

    @Test
    void testIsAccepted_ShouldReturnTrueWhenDescriptionAndTitleAreNotNull() {
        filter.setDescription("Some description");
        filter.setTitle("Test");

        boolean result = descriptionFilter.isAccepted(filter);

        assertTrue(result);
    }

    @Test
    void testIsAccepted_ShouldReturnFalseWhenDescriptionIsNull() {
        filter.setTitle("Test");

        boolean result = descriptionFilter.isAccepted(filter);

        assertFalse(result);
    }

    @Test
    void testIsAccepted_ShouldReturnFalseWhenTitleIsEmpty() {
        filter.setDescription("Some description");
        filter.setTitle("");

        boolean result = descriptionFilter.isAccepted(filter);

        assertFalse(result);
    }

    @Test
    void testApply_ShouldFilterAchievementsByTitle() {
        List<Achievement> correctAnswer = List.of(achievement1);
        filter.setTitle("Test Achievement 1");

        Stream<Achievement> achievementStream = Stream.of(achievement1, achievement2);
        List<Achievement> result = descriptionFilter.apply(achievementStream, filter).toList();

        assertEquals(correctAnswer, result);
    }

    @Test
    void testApply_ShouldReturnEmptyStreamWhenNoMatch() {
        filter.setTitle("Non-existent Achievement");

        Stream<Achievement> achievementStream = Stream.of(achievement1, achievement2);
        List<Achievement> result = descriptionFilter.apply(achievementStream, filter).toList();

        assertTrue(result.isEmpty());
    }
}

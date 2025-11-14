package faang.school.achievement.filters;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.service.DataForTests;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TitleFilterTest extends DataForTests {

    private final AchievementFilter filter = new TitleFilter();
    private final AchievementFilterDto achievementFilterDto = new AchievementFilterDto(TITLE,
            null,
            null);

    @Test
    void isApplicable_filterDoesNotExist() {
        AchievementFilterDto achievementFilterDto = new AchievementFilterDto(null,
                DESCRIPTION,
                null);

        assertFalse(filter.isApplicable(achievementFilterDto));
    }

    @Test
    void isApplicable_filterExists() {
        assertTrue(filter.isApplicable(achievementFilterDto));
    }

    @Test
    void apply_returnsAchievementsMatchingTitle() {

        List<Achievement> achievementsExpected = List.of(
                achievementCollector
        );

        List<Achievement> achievementActual = filter.apply(allAchievements
                        .stream(), achievementFilterDto)
                .toList();

        assertEquals(achievementActual.size(), achievementsExpected.size());
        assertEquals(new HashSet<>(achievementsExpected),
                new HashSet<>(achievementActual));
    }
}
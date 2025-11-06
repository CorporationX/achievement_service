package faang.school.achievement.filters;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.service.DataForTests;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class DescriptionFilterTest extends DataForTests {

    private final AchievementFilter achievementFilter = new descriptionFilter();
    private final AchievementFilterDto achievementFilterDto = new AchievementFilterDto(null,
            DESCRIPTION,
            null);

    @Test
    void isApplicable_filterDoesNotExist() {
        AchievementFilterDto achievementFilterDto = new AchievementFilterDto(null,
                null,
                Rarity.COMMON);

        assertFalse(achievementFilter.isApplicable(achievementFilterDto));
    }

    @Test
    void isApplicable_filterExists() {
        assertTrue(achievementFilter.isApplicable(achievementFilterDto));
    }

    @Test
    void apply_returnsAchievementsMatchingDescription() {
        List<Achievement> achievementsExpected = List.of(
                achievementCollector,
                achievementMrProductivity,
                achievementExpert,
                achievementWriter
        );

        List<Achievement> achievementActual = achievementFilter.apply(allAchievements
                        .stream(), achievementFilterDto)
                .toList();

        assertEquals(achievementActual.size(), achievementsExpected.size());
        assertEquals(new HashSet<>(achievementsExpected),
                new HashSet<>(achievementActual));
    }
}
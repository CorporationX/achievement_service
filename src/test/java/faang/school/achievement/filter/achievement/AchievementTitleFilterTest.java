package faang.school.achievement.filter.achievement;

import faang.school.achievement.dto.achievement.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AchievementTitleFilterTest {
    private final AchievementTitleFilter achievementTitleFilter = new AchievementTitleFilter();

    @Test
    public void testIsNotApplicable() {
        AchievementFilterDto filterDto = new AchievementFilterDto();

        assertFalse(achievementTitleFilter.isApplicable(filterDto));
    }

    @Test
    public void testIsApplicable() {
        AchievementFilterDto filterDto = new AchievementFilterDto();
        filterDto.setTitlePattern("Test");

        assertTrue(achievementTitleFilter.isApplicable(filterDto));
    }

    @Test
    public void testFilterEntityWithoutContainingRarity() {
        Achievement achievement = new Achievement();
        achievement.setTitle("Test");
        AchievementFilterDto filterDto = new AchievementFilterDto();
        filterDto.setTitlePattern("Name");

        assertFalse(achievementTitleFilter.filterEntity(achievement, filterDto));
    }

    @Test
    public void testFilterEntityWithContainingRarity() {
        Achievement achievement = new Achievement();
        achievement.setTitle("Test");
        AchievementFilterDto filterDto = new AchievementFilterDto();
        filterDto.setTitlePattern("Test");

        assertTrue(achievementTitleFilter.filterEntity(achievement, filterDto));
    }
}

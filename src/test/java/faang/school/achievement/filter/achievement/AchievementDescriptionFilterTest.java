package faang.school.achievement.filter.achievement;

import faang.school.achievement.dto.achievement.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AchievementDescriptionFilterTest {
    private final AchievementDescriptionFilter achievementDescriptionFilter = new AchievementDescriptionFilter();

    @Test
    public void testIsNotApplicable() {
        AchievementFilterDto filterDto = new AchievementFilterDto();

        assertFalse(achievementDescriptionFilter.isApplicable(filterDto));
    }

    @Test
    public void testIsApplicable() {
        AchievementFilterDto filterDto = new AchievementFilterDto();
        filterDto.setDescriptionPattern("Test");

        assertTrue(achievementDescriptionFilter.isApplicable(filterDto));
    }

    @Test
    public void testFilterEntityWithoutContainingRarity() {
        Achievement achievement = new Achievement();
        achievement.setDescription("Test");
        AchievementFilterDto filterDto = new AchievementFilterDto();
        filterDto.setDescriptionPattern("Description");

        assertFalse(achievementDescriptionFilter.filterEntity(achievement, filterDto));
    }

    @Test
    public void testFilterEntityWithContainingRarity() {
        Achievement achievement = new Achievement();
        achievement.setDescription("Test");
        AchievementFilterDto filterDto = new AchievementFilterDto();
        filterDto.setDescriptionPattern("Test");

        assertTrue(achievementDescriptionFilter.filterEntity(achievement, filterDto));
    }
}

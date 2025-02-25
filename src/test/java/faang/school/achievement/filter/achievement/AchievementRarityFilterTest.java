package faang.school.achievement.filter.achievement;

import faang.school.achievement.dto.achievement.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.Rarity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AchievementRarityFilterTest {
    private final AchievementRarityFilter achievementRarityFilter = new AchievementRarityFilter();

    @Test
    public void testIsNotApplicable() {
        AchievementFilterDto filterDto = new AchievementFilterDto();

        assertFalse(achievementRarityFilter.isApplicable(filterDto));
    }

    @Test
    public void testIsApplicable() {
        AchievementFilterDto filterDto = new AchievementFilterDto();
        filterDto.setRarity(Rarity.RARE);

        assertTrue(achievementRarityFilter.isApplicable(filterDto));
    }

    @Test
    public void testFilterEntityWithoutContainingRarity() {
        Achievement achievement = new Achievement();
        achievement.setRarity(Rarity.EPIC);
        AchievementFilterDto filterDto = new AchievementFilterDto();
        filterDto.setRarity(Rarity.RARE);

        assertFalse(achievementRarityFilter.filterEntity(achievement, filterDto));
    }

    @Test
    public void testFilterEntityWithContainingRarity() {
        Achievement achievement = new Achievement();
        achievement.setRarity(Rarity.RARE);
        AchievementFilterDto filterDto = new AchievementFilterDto();
        filterDto.setRarity(Rarity.RARE);

        assertTrue(achievementRarityFilter.filterEntity(achievement, filterDto));
    }
}

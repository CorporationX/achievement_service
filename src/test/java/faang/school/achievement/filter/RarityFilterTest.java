package faang.school.achievement.filter;

import static org.junit.jupiter.api.Assertions.*;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.Rarity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

class RarityFilterTest {

    private RarityFilter rarityFilter;
    private AchievementFilterDto filter;
    private Achievement achievement1;
    private Achievement achievement2;

    @BeforeEach
    void setUp() {
        rarityFilter = new RarityFilter();
        filter = new AchievementFilterDto();
        achievement1 = new Achievement();
        achievement2 = new Achievement();

        achievement1.setRarity(Rarity.COMMON);
        achievement2.setRarity(Rarity.RARE);
    }

    @Test
    void testIsAccepted_ShouldReturnTrueWhenRarityIsNotNull() {
        filter.setRarity(Rarity.COMMON);

        boolean result = rarityFilter.isAccepted(filter);

        assertTrue(result);
    }

    @Test
    void testIsAccepted_ShouldReturnFalseWhenRarityIsNull() {
        boolean result = rarityFilter.isAccepted(filter);

        assertFalse(result);
    }

    @Test
    void testApply_ShouldFilterAchievementsByRarity() {
        List<Achievement> correctAnswer = List.of(achievement1);
        filter.setRarity(Rarity.COMMON);

        Stream<Achievement> achievementStream = Stream.of(achievement1, achievement2);
        List<Achievement> result = rarityFilter.apply(achievementStream, filter).toList();

        assertEquals(correctAnswer, result);
    }

    @Test
    void testApply_ShouldReturnEmptyStreamWhenNoMatch() {
        filter.setRarity(Rarity.EPIC);

        Stream<Achievement> achievementStream = Stream.of(achievement1, achievement2);
        List<Achievement> result = rarityFilter.apply(achievementStream, filter).toList();

        assertTrue(result.isEmpty());
    }

    @Test
    void testApply_ShouldReturnAllAchievementsForMatchingRarity() {
        achievement1.setRarity(Rarity.RARE);
        achievement2.setRarity(Rarity.RARE);
        List<Achievement> correctAnswer = List.of(achievement1, achievement2);
        filter.setRarity(Rarity.RARE);

        Stream<Achievement> achievementStream = Stream.of(achievement1, achievement2);
        List<Achievement> result = rarityFilter.apply(achievementStream, filter).toList();

        assertEquals(correctAnswer, result);
    }
}

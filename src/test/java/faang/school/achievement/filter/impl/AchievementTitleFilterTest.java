package faang.school.achievement.filter.impl;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.filter.AchievementFilter;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.util.AchievementTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AchievementTitleFilterTest {
    private AchievementFilter achievementFilter = new AchievementTitleFilter();
    private AchievementTestContainer container;
    private AchievementFilterDto filters;

    @BeforeEach
    void setUp() {
        container = new AchievementTestContainer();
        filters = AchievementFilterDto.builder()
                .title("filter")
                .build();
    }

    @Test
    void isApplicable() {
        AchievementFilterDto nullFilters = new AchievementFilterDto();

        assertFalse(achievementFilter.isApplicable(nullFilters));
        assertTrue(achievementFilter.isApplicable(filters));
    }

    @Test
    void testMethod() {
        // then
        Achievement achievementTrue = Achievement.builder()
                .title(container.title() + filters.getTitle())
                .build();

        Achievement achievementFalse = Achievement.builder()
                .title(container.description())
                .build();

        // when
        assertTrue(achievementFilter.test(achievementTrue, filters));
        assertFalse(achievementFilter.test(achievementFalse, filters));
    }
}
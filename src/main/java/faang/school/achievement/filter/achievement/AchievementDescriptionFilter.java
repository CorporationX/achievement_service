package faang.school.achievement.filter.achievement;

import faang.school.achievement.dto.achievement.AchievementFilterDto;
import faang.school.achievement.model.Achievement;

public class AchievementDescriptionFilter implements AchievementFilter {
    @Override
    public boolean isApplicable(AchievementFilterDto filters) {
        return filters.titlePattern() != null;
    }

    @Override
    public boolean filterEntity(Achievement achievement, AchievementFilterDto filters) {
        return achievement.getDescription().contains(filters.descriptionPattern());
    }
}

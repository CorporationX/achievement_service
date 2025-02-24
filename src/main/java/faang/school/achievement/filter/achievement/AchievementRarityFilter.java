package faang.school.achievement.filter.achievement;

import faang.school.achievement.dto.achievement.AchievementFilterDto;
import faang.school.achievement.model.Achievement;

public class AchievementRarityFilter implements AchievementFilter {
    @Override
    public boolean isApplicable(AchievementFilterDto filters) {
        return filters.getRarity() != null;
    }

    @Override
    public boolean filterEntity(Achievement achievement, AchievementFilterDto filters) {
        return achievement.getRarity() == filters.getRarity();
    }
}

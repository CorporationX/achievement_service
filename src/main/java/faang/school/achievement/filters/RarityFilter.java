package faang.school.achievement.filters;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;


@Component
public class RarityFilter implements AchievementFilter {

    @Override
    public boolean isApplicable(AchievementFilterDto filters) {
        return filters.rarity() != null;
    }

    @Override
    public Stream<Achievement> apply(Stream<Achievement> achievements, AchievementFilterDto filters) {
        return achievements.filter(achievement -> achievement.getRarity() == filters.rarity());
    }
}
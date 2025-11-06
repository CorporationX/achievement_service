package faang.school.achievement.filters;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class rarityFilter implements AchievementFilter {

    @Override
    public boolean isApplicable(AchievementFilterDto filters) {
        return filters.rarity() != null;
    }

    @Override
    public Stream<Achievement> apply(Stream<Achievement> users, AchievementFilterDto filters) {
        return users.filter(achievement -> achievement.getRarity() == filters.rarity());
    }
}
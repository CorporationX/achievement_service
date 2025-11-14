package faang.school.achievement.filters;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class DescriptionFilter implements AchievementFilter {

    @Override
    public boolean isApplicable(AchievementFilterDto filters) {
        return filters.description() != null;
    }

    @Override
    public Stream<Achievement> apply(Stream<Achievement> users, AchievementFilterDto filters) {
        return users.filter(achievement -> achievement
                .getDescription()
                .toLowerCase()
                .contains(filters
                        .description()
                        .toLowerCase()));
    }
}
package faang.school.achievement.filter.achievement;

import faang.school.achievement.dto.achievement.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class AchievementTitleFilter implements AchievementFilter {

    @Override
    public boolean isAcceptable(AchievementFilterDto filters) {
        return filters.getTitlePrefix() != null && !filters.getTitlePrefix().isBlank();
    }

    @Override
    public Stream<Achievement> apply(Stream<Achievement> achievements, AchievementFilterDto filters) {
        return achievements.filter(achievement -> achievement.getTitle().startsWith(filters.getTitlePrefix()));
    }
}
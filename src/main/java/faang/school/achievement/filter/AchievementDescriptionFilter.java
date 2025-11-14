package faang.school.achievement.filter;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class AchievementDescriptionFilter implements AchievementFilter {
    @Override
    public boolean isApplicable(AchievementFilterDto filterDto) {
        return filterDto.description() != null && !filterDto.description().trim().isEmpty();
    }

    @Override
    public Stream<Achievement> apply(Stream<Achievement> achievements, AchievementFilterDto filterDto) {
        String description = filterDto.description().toLowerCase();
        return achievements.filter(achievement -> achievement.getDescription().toLowerCase().contains(description));
    }
}

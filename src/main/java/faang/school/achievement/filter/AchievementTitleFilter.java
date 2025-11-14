package faang.school.achievement.filter;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class AchievementTitleFilter implements AchievementFilter {
    @Override
    public boolean isApplicable(AchievementFilterDto filterDto) {
        return filterDto.title() != null && !filterDto.title().trim().isEmpty();
    }

    @Override
    public Stream<Achievement> apply(Stream<Achievement> achievements, AchievementFilterDto filterDto) {
        String title = filterDto.title().toLowerCase();
        return achievements.filter(achievement ->
                achievement.getTitle().toLowerCase().contains(title)
        );
    }
}

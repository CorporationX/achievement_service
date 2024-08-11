package faang.school.achievement.filter;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class AchievementTitleFilter implements AchievementFilter {
    @Override
    public boolean isApplicable(AchievementFilterDto achievementFilterDto) {
        return (achievementFilterDto.getTitle() != null && !achievementFilterDto.getTitle().isBlank());
    }

    @Override
    public Stream<Achievement> applyFilter(Stream<Achievement> achievements, AchievementFilterDto achievementFilterDto) {
        return achievements.filter(achievement -> achievement.getTitle().contains(achievementFilterDto.getTitle()));
    }
}
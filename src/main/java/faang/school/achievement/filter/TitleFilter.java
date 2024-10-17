package faang.school.achievement.filter;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class TitleFilter implements AchievementFilter {

    @Override
    public boolean isAccepted(AchievementFilterDto filter) {
        return filter != null && filter.getTitle() != null && !filter.getTitle().isEmpty();
    }

    @Override
    public Stream<Achievement> apply(Stream<Achievement> achievementStream, AchievementFilterDto filter) {
        return achievementStream.filter(achievement ->
                achievement.getTitle().equalsIgnoreCase(filter.getTitle()));
    }
}

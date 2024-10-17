package faang.school.achievement.filter;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class RarityFilter implements AchievementFilter {

    @Override
    public boolean isAccepted(AchievementFilterDto filter) {
        return filter != null && filter.getRarity() != null;
    }

    @Override
    public Stream<Achievement> apply(Stream<Achievement> achievementStream, AchievementFilterDto filter) {
        return achievementStream.filter(achievement ->
                achievement.getRarity().equals(filter.getRarity()));
    }
}

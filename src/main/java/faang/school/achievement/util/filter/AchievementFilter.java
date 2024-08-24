package faang.school.achievement.util.filter;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.model.Achievement;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public interface AchievementFilter {

    boolean isApplicable(AchievementFilterDto filters);

    Stream<Achievement> apply(Stream<Achievement> achievementStream, AchievementFilterDto filters);
}

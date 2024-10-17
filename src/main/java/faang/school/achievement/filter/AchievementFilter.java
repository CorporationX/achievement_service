package faang.school.achievement.filter;

import faang.school.achievement.dto.AchievementFilterDto;
import faang.school.achievement.model.Achievement;

import java.util.stream.Stream;

public interface AchievementFilter {

    boolean isAccepted(AchievementFilterDto filter);

    Stream<Achievement> apply(Stream<Achievement> achievementStream, AchievementFilterDto filter);
}

package faang.school.achievement.cache;

import faang.school.achievement.model.Achievement;

import java.util.Optional;

public interface AchievementCache {

    Optional<Achievement> get(String title);
}

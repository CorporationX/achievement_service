package faang.school.achievement.source;

import faang.school.achievement.model.Achievement;

import java.util.List;
import java.util.Optional;

public interface AchievementSource {
    List<Achievement> getAll();

    Optional<Achievement> getByTitle(String title);

    Optional<Achievement> getById(long id);
}
